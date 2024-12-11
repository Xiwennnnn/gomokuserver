package whpu.gomoku.gomokuserver.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class QuitRoomEvent extends ApplicationEvent {
    private Long userId;
    private Long roomId;
    public QuitRoomEvent(Long userId, Long roomId) {
        super(userId);
        this.userId = userId;
        this.roomId = roomId;
    }
}
