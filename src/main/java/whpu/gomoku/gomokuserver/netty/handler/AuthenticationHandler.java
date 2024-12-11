package whpu.gomoku.gomokuserver.netty.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.entity.User;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.data.response.data.LoginSuccessData;
import whpu.gomoku.gomokuserver.data.response.data.RegisterSuccessData;
import whpu.gomoku.gomokuserver.exception.authen.ExistingUsernameException;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.service.AuthenticationService;
import whpu.gomoku.util.JwtUtil;

@Slf4j
@ChannelHandler.Sharable
@Component
public class AuthenticationHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    ThreadLocal<ObjectMapper> objMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    @Resource
    private AuthenticationService authenticationService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String upgradeHeader = request.headers().get(HttpHeaderNames.UPGRADE);
        if ("websocket".equalsIgnoreCase(upgradeHeader) || !"POST".equals(request.method().name())) {
            request.content().retain();
            ctx.fireChannelRead(request);
            return;
        }
        ByteBuf byteBuf = request.content();
        byte[] byteArray = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(), byteArray);
        JsonNode json = objMapperThreadLocal.get().readTree(byteArray);
        if ("/login".equals(request.uri())) {
            String username = json.get("username").asText();
            String password = json.get("password").asText();
            User user = authenticationService.authenticate(username, password);
            if (user!= null) {
                LoginSuccessData loginSuccessData = new LoginSuccessData(
                        JwtUtil.generateToken(user));
                FullHttpResponse httpResponse = authenticationSuccessResponse(loginSuccessData);
                ctx.writeAndFlush(httpResponse);
            } else {
                log.warn("Authentication failed: username={}, password={}", username, password);
                GomokuResponse response = new GomokuResponse(ErrorCode.AUTHENTICATION_ERROR);
                String jsonResponse = objMapperThreadLocal.get().writeValueAsString(response);
                ByteBuf content = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
                FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.UNAUTHORIZED,
                        content
                );
                httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
                httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
                ctx.writeAndFlush(httpResponse);
            }
        } else if ("/register".equals(request.uri())) {
            String username = json.get("username").asText();
            String password = json.get("password").asText();
            User user;
            try {
                user = authenticationService.register(username, password);
            } catch (ExistingUsernameException existingUsernameException) {
                log.warn("Username already exists: {}", username);
                GomokuResponse response = new GomokuResponse(ErrorCode.USERNAME_EXIST_ERROR);
                String jsonResponse = objMapperThreadLocal.get().writeValueAsString(response);
                ByteBuf content = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
                FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.CONFLICT,
                        content
                );
                httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
                httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
                ctx.writeAndFlush(httpResponse);
                return;
            }
            if (user != null) {
                RegisterSuccessData registerSuccessData = new RegisterSuccessData(
                    JwtUtil.generateToken(user));
                FullHttpResponse httpResponse = authenticationSuccessResponse(registerSuccessData);
                ctx.writeAndFlush(httpResponse);
            }
        } else {
            request.content().retain();
            ctx.fireChannelRead(request);
        }
    }

    public FullHttpResponse authenticationSuccessResponse(Object data) throws JsonProcessingException {
        GomokuResponse response = new GomokuResponse(200, ResponseActionType.LOGIN_SUCCESS, data);
        String jsonResponse = objMapperThreadLocal.get().writeValueAsString(response);
        ByteBuf content = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                content
        );
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        return httpResponse;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("Exception caught in AuthenticationHandler", cause);
        GomokuResponse response = new GomokuResponse(ErrorCode.REQUEST_FORMAT_ERROR);
        String jsonResponse = objMapperThreadLocal.get().writeValueAsString(response);
        ByteBuf content = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.BAD_REQUEST,
                content
        );
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.writeAndFlush(httpResponse);
        ctx.close();
    }
}
