package whpu.gomoku.gomokuserver.listener;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.GomokuContext;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.data.response.data.GetRoomsData;
import whpu.gomoku.gomokuserver.listener.event.*;
import whpu.gomoku.gomokuserver.service.GameService;
import whpu.gomoku.gomokuserver.service.RoomService;
import whpu.gomoku.gomokuserver.service.UserService;
import whpu.gomoku.gomokuserver.service.action.common.PublicGameStatusAction;
import whpu.gomoku.gomokuserver.service.action.common.PublishRoomStatusAction;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class AuthenticationListener {
    @Resource
    private UserService userService;
    @Resource
    private RoomService roomService;
    @Resource
    private GameService gameService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Async
    @EventListener
    public void loginSuccess(LoginSuccessEvent event) {
        UserDto user = userService.getUserById(event.getUserId());
        GomokuContext.userChannelMap.get(event.getUserId()).writeAndFlush(
                new TextWebSocketFrame(
                        new GomokuResponse(ResponseActionType.LOGIN_SUCCESS, user).toJsonString()
                )
        );
        userService.setOline(event.getUserId());
    }

    @Async
    @EventListener
    public void offline(OfflineEvent event) {
        log.info("offline: " + event.getUserId());
        UserDto user = userService.getUserById(event.getUserId());
        if (user.getStatus() == 3) { // 游戏中
            GameDto game = gameService.getGameByBlackId(event.getUserId());
            if (game != null) {
                gameOver(new GameOverEvent(game.getWhitePlayerId(), game.getId()));
            } else {
                GameDto _game = gameService.getGameByWhiteId(event.getUserId());
                gameOver(new GameOverEvent(_game.getBlackPlayerId(), _game.getId()));
            }
        }
        roomService.exitRoom(user);
        userService.setOffline(event.getUserId());
        GomokuContext.userChannelMap.remove(event.getUserId());
        GomokuContext.userIdMap.values().remove(event.getUserId());
    }

    @Async
    @EventListener
    public void logoutSuccess(LogoutSuccessEvent event) {
        userService.setOffline(event.getUserId());
    }

    @Async
    @EventListener
    public void enterRoom(EnterRoomEvent event) {
        RoomDto roomDto = roomService.getRoomById(event.getRoomId());
        if (roomDto!= null) {
            Long ownerId = roomDto.getOwnerId();
            PublishRoomStatusAction.publishRoomStatus(roomDto, ownerId);
        }
        List<RoomDto> rooms = roomService.getAliveRoomList();
        PublishRoomStatusAction.publishRoomUpdate(rooms);
        userService.setInRoom(event.getUserId());
    }

    @Async
    @EventListener
    public void quitRoom(QuitRoomEvent event) {
        List<RoomDto> rooms = roomService.getAliveRoomList();
        PublishRoomStatusAction.publishRoomUpdate(rooms);
        userService.setOline(event.getUserId());
    }

    @Async
    @EventListener
    public void enterGame(EnterGameEvent event) {
        userService.setInGame(event.getUserId());
    }

    @Async
    @EventListener
    public void quitGame(QuitGameEvent event) {
        userService.setOline(event.getUserId());
    }

    @Async
    @EventListener
    public void gameOver(GameOverEvent event) {
        GameDto game = gameService.getGameById(event.getGameId());
        UserDto winner = userService.getUserById(event.getWinnerId());
        PublicGameStatusAction.publicGameOver(game, winner.getHandle());
    }
}