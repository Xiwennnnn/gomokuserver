package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.event.room.BeginGameRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.service.GameService;
import whpu.gomoku.gomokuserver.service.RoomService;
import whpu.gomoku.gomokuserver.service.action.common.PublicGameStatusAction;

@Component
@Slf4j
public class BeginGameAction implements Action<BeginGameRequest> {
    @Resource
    private RoomService roomService;
    @Resource
    private GameService gameService;
    @Override
    public String getActionType() {
        return ActionEnum.GAME_START.action;
    }
    @Override
    public GomokuResponse doAction(BeginGameRequest req, Long userId) {
        RoomDto room = roomService.getRoomById(req.getRoomId());
        if (room == null) {
            return new GomokuResponse(ErrorCode.ROOM_NOT_EXIST_ERROR);
        }
        if (room.getPlayerId() == null) {
            return new GomokuResponse(ErrorCode.USER_NOT_IN_ROOM);
        }
        gameService.startGame(room);
        return null;
    }
}
