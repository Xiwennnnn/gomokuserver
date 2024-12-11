package whpu.gomoku.gomokuserver.data.event.game;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class UndoRequest extends AbstractRequest {
    private Long gameId;
    @Override
    public String getActionType() {
        return ActionEnum.UNDO.action;
    }
    public static UndoRequest fromJson(JsonNode json) {
        UndoRequest request = new UndoRequest();
        request.setGameId(json.get("gameId").asLong());
        return request;
    }
}
