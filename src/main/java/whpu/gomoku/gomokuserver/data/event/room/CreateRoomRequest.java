package whpu.gomoku.gomokuserver.data.event.room;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateRoomRequest extends AbstractRequest {
    @Override
    public String getActionType() {
        return ActionEnum.CREATE_ROOM.action;
    }

    public static CreateRoomRequest fromJson(JsonNode json) {
        return new CreateRoomRequest();
    }
}
