package whpu.gomoku.gomokuserver.netty.runner;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.GomokuContext;
import whpu.gomoku.gomokuserver.data.common.GomokuConstant;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.netty.handler.AuthenticationHandler;
import whpu.gomoku.gomokuserver.netty.handler.HeatbeatHandler;
import whpu.gomoku.gomokuserver.netty.handler.JwtAuthenticationTokenHandler;
import whpu.gomoku.gomokuserver.netty.handler.WebsocketActionHandler;
import whpu.gomoku.gomokuserver.service.GameService;
import whpu.gomoku.gomokuserver.service.RoomService;

import java.net.InetSocketAddress;

@Component
@Slf4j
@EnableConfigurationProperties
public class NettyBootstrapRunner implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {
    @Value("${netty.websocket.port}")
    private int port;
    @Value("${netty.websocket.ip}")
    private String ip;
    @Value("${netty.websocket.path}")
    private String path;
    @Value("${netty.websocket.max-frame-size}")
    private long maxFrameSize;
    @Resource
    private ApplicationContext applicationContext;
    private Channel serverChannel;
    @Resource
    private AuthenticationHandler authenticationHandler;
    @Resource
    private WebsocketActionHandler websocketActionHandler;
    @Resource
    private JwtAuthenticationTokenHandler jwtAuthenticationTokenHandler;
    @Resource
    private GameService gameService;
    @Resource
    private RoomService roomService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress(this.ip, this.port));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new ChunkedWriteHandler());
                    pipeline.addLast(new HttpObjectAggregator(65536));
                    // 超过一定时间没有读写数据，则触发 IdleStateHandler 事件
                    pipeline.addLast(new IdleStateHandler(GomokuConstant.HEARTBEAT_TIMEOUT, 0, 0));
                    // 自定义心跳检测
                    pipeline.addLast(new HeatbeatHandler());
                    // 用户处理http认证请求
                    pipeline.addLast(authenticationHandler);
                    // 用于处理 404 错误
                    pipeline.addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            if (msg instanceof FullHttpRequest request) {
                                String uri = request.uri();
                                if (!uri.equals(path)) {
                                    ctx.channel().writeAndFlush(new DefaultFullHttpResponse(
                                            HttpVersion.HTTP_1_1,
                                            HttpResponseStatus.NOT_FOUND)
                                    ).addListener(ChannelFutureListener.CLOSE);
                                    return;
                                }
                            }
                            if (msg instanceof FullHttpRequest) {
                                ((FullHttpRequest) msg).content().retain();
                            }
                            ctx.fireChannelRead(msg);
                        }
                    });
                    // JwtAuthenticationTokenHandler 用于处理 JWT 验证
                    pipeline.addLast(jwtAuthenticationTokenHandler);
                    pipeline.addLast(new WebSocketServerCompressionHandler());
                    pipeline.addLast(new WebSocketServerProtocolHandler(
                            path, null, false));
                    pipeline.addLast(websocketActionHandler);
                }
            });
            Channel channel = serverBootstrap.bind().sync().channel();
            this.serverChannel = channel;
            log.info("Netty WebSocket Server Started on ip:{}, port:{}", this.ip, this.port);
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        if (this.serverChannel!= null) {
            for (Channel channel : GomokuContext.userChannelMap.values()) {
                // 通知所有用户服务器关闭
                channel.writeAndFlush(new CloseWebSocketFrame(
                        ErrorCode.SERVER_HAS_CLOSED.getCode(),
                        ErrorCode.SERVER_HAS_CLOSED.getMessage()))
                    .addListener(future -> {
                            if (future.isSuccess()) {
                                log.info("关闭用户 {} 连接成功", channel.id().asShortText());
                                channel.close();
                            } else {
                                 log.error("关闭用户 {} 连接失败", channel.id().asShortText(), future.cause());
                            }
                });
            }
            this.serverChannel.close();
        }
        gameService.closeAllGame();
        roomService.closeAllRooms();
        log.info("Netty WebSocket Server Stopped");
    }
}
