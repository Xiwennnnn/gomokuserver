package whpu.gomoku.gomokuserver.service.action;

import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;

public interface Action<T extends AbstractRequest> {
    public String getActionType();
    public GomokuResponse doAction(T req, Long userId);
}
