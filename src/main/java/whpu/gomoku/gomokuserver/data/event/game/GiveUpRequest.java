package whpu.gomoku.gomokuserver.data.event.game;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class GiveUpRequest extends AbstractRequest {
    private Long gameId;
    private Long playerId;
    @Override
    public String getActionType() {
        return ActionEnum.GIVE_UP.action;
    }
    public static GiveUpRequest fromJson(JsonNode json) {
        GiveUpRequest request = new GiveUpRequest();
        request.gameId = json.get("gameId").asLong();
        request.playerId = json.get("playerId").asLong();
        return request;
    }
}
