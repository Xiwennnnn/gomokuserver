package whpu.gomoku.gomokuserver.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class EnterGameEvent extends ApplicationEvent {
    private Long userId;
    public EnterGameEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }
}
