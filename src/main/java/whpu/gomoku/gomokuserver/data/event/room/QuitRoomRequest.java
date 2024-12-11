package whpu.gomoku.gomokuserver.data.event.room;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuitRoomRequest extends AbstractRequest {
    private Long roomId;
    @Override
    public String getActionType() {
        return ActionEnum.QUIT_ROOM.action;
    }
    public static QuitRoomRequest fromJson(JsonNode json) {
        QuitRoomRequest request = new QuitRoomRequest();
        request.roomId = json.get("roomId").asLong();
        return request;
    }
}
