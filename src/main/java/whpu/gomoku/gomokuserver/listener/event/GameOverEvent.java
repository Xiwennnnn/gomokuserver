package whpu.gomoku.gomokuserver.listener.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class GameOverEvent extends ApplicationEvent {
    private Long winnerId;
    private Long gameId;
    public GameOverEvent(Long winnerId, Long gameId) {
        super(winnerId);
        this.winnerId = winnerId;
        this.gameId = gameId;
    }
}
