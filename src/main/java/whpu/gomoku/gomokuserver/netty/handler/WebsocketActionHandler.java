package whpu.gomoku.gomokuserver.netty.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.GomokuContext;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;
import whpu.gomoku.gomokuserver.data.event.EventFactory;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.listener.event.OfflineEvent;
import whpu.gomoku.gomokuserver.listener.event.QuitGameEvent;
import whpu.gomoku.gomokuserver.service.GomokuBusiness;
import whpu.gomoku.gomokuserver.service.Impl.UserServiceImpl;
import whpu.gomoku.gomokuserver.service.UserService;

@Slf4j
@Component
@ChannelHandler.Sharable
public class WebsocketActionHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    ThreadLocal<ObjectMapper> objMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Resource
    private GomokuBusiness gomokuBusiness;
    @Resource
    private UserService userService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        if (webSocketFrame instanceof TextWebSocketFrame msg) {
            JsonNode body;
            try {
                body = objMapperThreadLocal.get().readTree(msg.text());
                log.info("Received message: {}", msg.text());
            } catch (Exception e) {
                log.warn("Invalid message received: {}", msg.text());
                ctx.writeAndFlush(new TextWebSocketFrame(
                        new GomokuResponse(ErrorCode.JSON_FORMAT_ERROR).toJsonString()));
                return;
            }
            String action = body.get("action").asText();

            if (action == null || action.isEmpty()) {
                log.warn("UnHaving action filed from message received: {}", msg.text());
                ctx.writeAndFlush(new TextWebSocketFrame(
                        new GomokuResponse(ErrorCode.JSON_FORMAT_ERROR).toJsonString()));
                return;
            }
            AbstractRequest event = EventFactory.createEvent(body);
            GomokuResponse response = gomokuBusiness.doBusiness(
                    event,
                    GomokuContext.userIdMap.get(ctx.channel().hashCode()));
            if (response == null) {
                return;
            }
            TextWebSocketFrame responseMsg = new TextWebSocketFrame(response.toJsonString());
            ctx.channel().writeAndFlush(responseMsg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Long userId = GomokuContext.userIdMap.get(ctx.channel().hashCode());
        if (userId != null) {
            eventPublisher.publishEvent(new OfflineEvent(userId));
        }
    }
}
