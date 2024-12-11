package whpu.gomoku.gomokuserver.data.event.game;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class DropPieceRequest extends AbstractRequest {
    private Long gameId;
    private Integer index;
    @Override
    public String getActionType() {
        return ActionEnum.DROP_PIECE.action;
    }
    public static DropPieceRequest fromJson(JsonNode json) {
        DropPieceRequest request = new DropPieceRequest();
        request.setGameId(json.get("gameId").asLong());
        request.setIndex(json.get("index").asInt());
        return request;
    }
}
