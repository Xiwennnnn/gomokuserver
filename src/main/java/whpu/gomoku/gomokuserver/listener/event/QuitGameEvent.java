package whpu.gomoku.gomokuserver.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class QuitGameEvent extends ApplicationEvent {
    private Long userId;
    public QuitGameEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }
}
