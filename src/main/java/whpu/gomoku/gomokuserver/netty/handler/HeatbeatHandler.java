package whpu.gomoku.gomokuserver.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import whpu.gomoku.gomokuserver.data.common.GomokuConstant;

@Slf4j
public class HeatbeatHandler extends ChannelInboundHandlerAdapter {
    private static final int HEARTBEAT_TIMEOUT = GomokuConstant.HEARTBEAT_TIMEOUT;
    private long lastPongTime = System.currentTimeMillis();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PongWebSocketFrame) {
            lastPongTime = System.currentTimeMillis();
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                ctx.writeAndFlush(new PingWebSocketFrame());
            }
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPongTime > 2 * HEARTBEAT_TIMEOUT * 1000) {
            log.warn("client heartbeat timeout, close connection");
            ctx.close();
        }
        super.userEventTriggered(ctx, evt);
    }
}
