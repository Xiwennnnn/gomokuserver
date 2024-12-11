package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.event.game.UndoRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.service.GameService;
import whpu.gomoku.gomokuserver.service.action.common.PublicGameStatusAction;

@Component
public class UndoAction implements Action<UndoRequest> {
    @Resource
    private GameService gameService;
    @Override
    public String getActionType() {
        return ActionEnum.UNDO.action;
    }

    @Override
    public GomokuResponse doAction(UndoRequest req, Long userId) {
        GameDto game = gameService.getGameById(req.getGameId());
        Long opId = game.getBlackPlayerId().equals(userId) ? game.getWhitePlayerId() : game.getBlackPlayerId();
        PublicGameStatusAction.publicRequestUndo(opId);
        return null;
    }
}
