package whpu.gomoku.gomokuserver.data.event.room;

import com.fasterxml.jackson.databind.JsonNode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

public class GetRoomsRequest extends AbstractRequest {
    public String getActionType() {
        return ActionEnum.GET_ROOM_LIST.action;
    }

    public static GetRoomsRequest fromJson(JsonNode json) {
        return new GetRoomsRequest();
    }
}
