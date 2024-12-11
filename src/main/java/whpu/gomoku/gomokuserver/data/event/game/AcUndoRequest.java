package whpu.gomoku.gomokuserver.data.event.game;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class AcUndoRequest extends AbstractRequest {
    private Long gameId;
    private Boolean isAgree;
    @Override
    public String getActionType() {
        return ActionEnum.AC_UNDO.action;
    }
    public static AcUndoRequest fromJson(JsonNode json) {
        AcUndoRequest request = new AcUndoRequest();
        request.setGameId(json.get("gameId").asLong());
        request.setIsAgree(json.get("isAgree").asBoolean());
        return request;
    }
}
