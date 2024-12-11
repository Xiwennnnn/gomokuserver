package whpu.gomoku.gomokuserver.data.event.room;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;


@EqualsAndHashCode(callSuper = true)
@Data
public class EnterRoomRequest extends AbstractRequest {
    private Long roomId;
    @Override
    public String getActionType() {
        return ActionEnum.JOIN_ROOM.action;
    }
    public static EnterRoomRequest fromJson(JsonNode json) {
        EnterRoomRequest request = new EnterRoomRequest();
        request.roomId = json.get("roomId").asLong();
        return request;
    }
}
