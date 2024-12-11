package whpu.gomoku.gomokuserver.netty.handler;

import cn.hutool.http.Header;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.GomokuContext;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.data.response.data.ConnectSuccessData;
import whpu.gomoku.gomokuserver.exception.authen.InvalidTokenException;
import whpu.gomoku.gomokuserver.exception.authen.TokenExpiredException;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.listener.event.LoginSuccessEvent;
import whpu.gomoku.gomokuserver.service.UserService;
import whpu.gomoku.util.JwtUtil;

import java.nio.charset.StandardCharsets;


@Slf4j
@Component
@ChannelHandler.Sharable
public class JwtAuthenticationTokenHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Resource
    private UserService userService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String token = request.headers().get(Header.AUTHORIZATION.getValue());
        if (token!= null) {
            try {
                JwtUtil.validateToken(token);
            } catch (TokenExpiredException tokenExpiredException) { // token过期
                log.info("Remote ip {}, Token Expired", ctx.channel().remoteAddress());
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.UNAUTHORIZED,
                        Unpooled.copiedBuffer(ErrorCode.TOKEN_EXPIRED_ERROR.getMessage(), StandardCharsets.UTF_8)
                );
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                ctx.writeAndFlush(response).addListener(future -> {
                            if (future.isSuccess()) {
                                ctx.close();
                            }
                });
                return;
            } catch (InvalidTokenException invalidTokenException) { // token无效
                log.info("Remote ip {}, Token Invalid", ctx.channel().remoteAddress());
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.valueOf(ErrorCode.TOKEN_INVALID_ERROR.getCode(), ErrorCode.TOKEN_INVALID_ERROR.getMessage()),
                        Unpooled.copiedBuffer(ErrorCode.TOKEN_INVALID_ERROR.getMessage(), StandardCharsets.UTF_8)
                );
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                ctx.writeAndFlush(response).addListener(future -> {
                            if (future.isSuccess()) {
                                ctx.close();
                            }
                });
                return;
            }
            Long userId = JwtUtil.getId(token);
            GomokuContext.userChannelMap.put(userId, ctx.channel());
            GomokuContext.userIdMap.put(ctx.channel().hashCode(), userId);
            request.content().retain();
            ctx.fireChannelRead(request); // 验证通过，继续处理
            eventPublisher.publishEvent(new LoginSuccessEvent(userId));
        } else { // token不存在，需要登录
            log.info("Remote ip {}, User Not Login", ctx.channel().remoteAddress());
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(ErrorCode.USER_NOT_LOGIN_ERROR.getCode()),
                    Unpooled.copiedBuffer(ErrorCode.USER_NOT_LOGIN_ERROR.getMessage(), StandardCharsets.UTF_8)
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            ctx.writeAndFlush(response).addListener(future -> {
                        if (future.isSuccess()) {
                            ctx.close();
                        }
            });
        }
    }
}
