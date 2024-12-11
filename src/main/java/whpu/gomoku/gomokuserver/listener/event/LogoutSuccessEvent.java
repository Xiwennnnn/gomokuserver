package whpu.gomoku.gomokuserver.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class LogoutSuccessEvent extends ApplicationEvent {
    private Long userId;

    public LogoutSuccessEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }
}
