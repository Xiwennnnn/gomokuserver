package whpu.gomoku.gomokuserver.listener.event;

import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OfflineEvent extends ApplicationEvent {
    private Long userId;
    public OfflineEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }
}
