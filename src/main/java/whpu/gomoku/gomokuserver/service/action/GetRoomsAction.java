package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.room.GetRoomsRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.data.response.data.GetRoomsData;
import whpu.gomoku.gomokuserver.service.RoomService;

@Component
public class GetRoomsAction implements Action<GetRoomsRequest> {

    @Resource
    private RoomService roomService;

    @Override
    public String getActionType() {
        return ActionEnum.GET_ROOM_LIST.action;
    }

    @Override
    public GomokuResponse doAction(GetRoomsRequest req, Long userId) {
        return new GomokuResponse(ResponseActionType.UPDATE_ROOMS, new GetRoomsData(roomService.getAliveRoomList()));
    }
}