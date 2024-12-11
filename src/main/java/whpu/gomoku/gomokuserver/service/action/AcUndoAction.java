package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.event.game.AcUndoRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.service.GameService;

@Component
public class AcUndoAction implements Action<AcUndoRequest> {
    @Resource
    private GameService gameService;
    @Override
    public String getActionType() {
        return ActionEnum.AC_UNDO.action;
    }

    @Override
    public GomokuResponse doAction(AcUndoRequest req, Long userId) {
        GameDto game = gameService.getGameById(req.getGameId());
        gameService.undo(game);
        return null;
    }
}
