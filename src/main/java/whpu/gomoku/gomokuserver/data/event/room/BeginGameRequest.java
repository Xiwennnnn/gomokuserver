package whpu.gomoku.gomokuserver.data.event.room;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeginGameRequest extends AbstractRequest {
    private Long roomId;
    @Override
    public String getActionType() {
        return ActionEnum.GAME_START.action;
    }
    public static BeginGameRequest fromJson(JsonNode json) {
        BeginGameRequest request = new BeginGameRequest();
        request.setRoomId(json.get("roomId").asLong());
        return request;
    }
}
