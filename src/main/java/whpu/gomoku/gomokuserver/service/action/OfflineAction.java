package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.event.common.OfflineRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.listener.event.OfflineEvent;

@Component
public class OfflineAction implements Action<OfflineRequest> {
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Override
    public String getActionType() {
        return ActionEnum.OFFLINE.action;
    }

    @Override
    public GomokuResponse doAction(OfflineRequest req, Long userId) {
        eventPublisher.publishEvent(new OfflineEvent(userId));
        return null;
    }
}
