package whpu.gomoku.gomokuserver.data.event.common;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfflineRequest extends AbstractRequest {
    private Long userId;
    @Override
    public String getActionType() {
        return ActionEnum.OFFLINE.action;
    }
    public static OfflineRequest fromJson(JsonNode json) {
        OfflineRequest request = new OfflineRequest();
        request.setUserId(json.get("userId").asLong());
        return request;
    }
}
