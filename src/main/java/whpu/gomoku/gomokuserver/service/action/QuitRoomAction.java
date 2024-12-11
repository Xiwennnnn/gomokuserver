package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.event.room.QuitRoomRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.listener.event.QuitRoomEvent;
import whpu.gomoku.gomokuserver.service.RoomService;
import whpu.gomoku.gomokuserver.service.UserService;

@Component
@Slf4j
public class QuitRoomAction implements Action<QuitRoomRequest> {
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Override
    public String getActionType() {
        return ActionEnum.QUIT_ROOM.action;
    }

    @Override
    public GomokuResponse doAction(QuitRoomRequest req, Long userId) {
        RoomDto roomDto = roomService.quitRoom(userId, req.getRoomId());
        if (roomDto == null) {
            log.warn("Room {} already not exist, cannot quit.", req.getRoomId());
        }
        return new GomokuResponse(ResponseActionType.QUIT_ROOM_SUCCESS, roomDto);
    }
}
