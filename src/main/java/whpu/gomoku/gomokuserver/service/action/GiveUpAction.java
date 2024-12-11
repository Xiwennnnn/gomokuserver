package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.event.game.GiveUpRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.service.GameService;

@Component
public class GiveUpAction implements Action<GiveUpRequest> {
    @Resource
    private GameService gameService;
    @Override
    public String getActionType() {
        return ActionEnum.GIVE_UP.action;
    }

    @Override
    public GomokuResponse doAction(GiveUpRequest req, Long userId) {
        GameDto gameDto = gameService.getGameById(req.getGameId());
        gameService.endGame(gameDto, req.getPlayerId());
        return null;
    }
}
