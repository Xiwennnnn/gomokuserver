package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.event.room.EnterRoomRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.listener.event.EnterRoomEvent;
import whpu.gomoku.gomokuserver.service.RoomService;
import whpu.gomoku.gomokuserver.service.UserService;

@Component
@Slf4j
public class EnterRoomAction implements Action<EnterRoomRequest> {
    @Resource
    private UserService userService;
    @Resource
    private RoomService roomService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public String getActionType() {
        return ActionEnum.JOIN_ROOM.action;
    }

    @Override
    public GomokuResponse doAction(EnterRoomRequest req, Long userId) {
        UserDto user = userService.getUserById(userId);
        RoomDto room = roomService.getRoomById(req.getRoomId());
        if (user.getStatus() == 1) {
            if (room.getPlayerCount() == 2) {
                return new GomokuResponse(ErrorCode.ROOM_ALREADY_FULL);
            }
            switch (room.getStatus()) {
                case 0:
                    RoomDto newRoom = roomService.joinRoom(user, room.getId());
                    eventPublisher.publishEvent(new EnterRoomEvent(userId, room.getId()));
                    return new GomokuResponse(ResponseActionType.JOIN_ROOM_SUCCESS, newRoom);
                case 1:
                    return new GomokuResponse(ErrorCode.ROOM_ALREADY_STARTED);
                case 2:
                    return new GomokuResponse(ErrorCode.ROOM_NOT_EXIST_ERROR);
            }
        }
        return new GomokuResponse(ErrorCode.USER_ALREADY_IN_ROOM);
    }
}
