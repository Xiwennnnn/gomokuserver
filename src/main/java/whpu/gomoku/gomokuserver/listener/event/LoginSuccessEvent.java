package whpu.gomoku.gomokuserver.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class LoginSuccessEvent extends ApplicationEvent {
    private Long userId;

    public LoginSuccessEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }
}
