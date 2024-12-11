package whpu.gomoku.gomokuserver.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EnterRoomEvent extends ApplicationEvent {
    private Long userId;
    private Long roomId;
    public EnterRoomEvent(Long userId, Long roomId) {
        super(userId);
        this.userId = userId;
        this.roomId = roomId;
    }
}
