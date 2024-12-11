package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.event.room.CreateRoomRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.data.response.data.CreateRoomSuccessData;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.listener.event.EnterRoomEvent;
import whpu.gomoku.gomokuserver.service.RoomService;
import whpu.gomoku.gomokuserver.service.UserService;

@Component
public class CreateRoomAction implements Action<CreateRoomRequest> {
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public String getActionType() {
        return ActionEnum.CREATE_ROOM.action;
    }

    @Override
    public GomokuResponse doAction(CreateRoomRequest req, Long userId) {
        UserDto user = userService.getUserById(userId);
        if (user.getStatus() == 1) {
            RoomDto room = roomService.createRoom(user);
            eventPublisher.publishEvent(new EnterRoomEvent(userId, room.getId()));
            return new GomokuResponse(ResponseActionType.CREATE_ROOM_SUCCESS, new CreateRoomSuccessData(room));
        } else {
            return new GomokuResponse(ErrorCode.USER_ALREADY_IN_ROOM);
        }
    }
}
