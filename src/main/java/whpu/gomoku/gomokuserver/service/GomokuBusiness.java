package whpu.gomoku.gomokuserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.event.AbstractRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.service.action.Action;

import java.util.List;

@Slf4j
@Component
public class GomokuBusiness {
    @Autowired
    private List<Action<? extends AbstractRequest>> actions;

    public GomokuResponse doBusiness(AbstractRequest event, Long userId) {
        for (Action<? extends AbstractRequest> action : actions) {
            if (action.getActionType().equals(event.getActionType())) {
                return doActionSafely(action, event, userId);
            }
        }
        return null;
    }

    private <T extends AbstractRequest> GomokuResponse doActionSafely(Action<T> action, AbstractRequest event, Long userId) {
        @SuppressWarnings("unchecked")
        T castedEvent = (T) event; // 强制转换为泛型类型
        return action.doAction(castedEvent, userId);
    }
}

