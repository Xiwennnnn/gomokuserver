package whpu.gomoku.gomokuserver.service.action;

import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.ActionEnum;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.dto.GameMoveDto;
import whpu.gomoku.gomokuserver.data.event.game.DropPieceRequest;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.listener.event.GameOverEvent;
import whpu.gomoku.gomokuserver.service.GameService;
import whpu.gomoku.gomokuserver.service.action.common.PublicGameStatusAction;

@Component
public class DropPieceAction implements Action<DropPieceRequest> {
    @Resource
    private GameService gameService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public String getActionType() {
        return ActionEnum.DROP_PIECE.action;
    }

    @Override
    public GomokuResponse doAction(DropPieceRequest req, Long userId) {
        GameDto game = gameService.getGameById(req.getGameId());
        game.setMoveCount(game.getMoveCount() + 1);
        char color;
        if (game.getMoveCount() % 2 == 0) {
            game.getGameStatus().setCharAt(req.getIndex(), 'B');
            color = 'B';
        } else {
            game.getGameStatus().setCharAt(req.getIndex(), 'W');
            color = 'W';
        }
        GameMoveDto gameMove = new GameMoveDto();
        gameMove.setGameId(req.getGameId());
        gameMove.setPlayerId(userId);
        gameMove.setIsUndo(false);
        gameMove.setMoveNumber(game.getMoveCount());
        gameMove.setMovePosition(req.getIndex());
        gameService.gameMove(game, gameMove);
        PublicGameStatusAction.publicDropPiece(gameMove, game);
        int flag = isWin(game.getGameStatus().toString(), req.getIndex(), color);
        if (flag == 1) {
            gameService.endGame(game, gameMove.getPlayerId());
        } else if (flag == 2) {
            Long winnerId = game.getBlackPlayerId().equals(gameMove.getPlayerId()) ? game.getWhitePlayerId() : game.getBlackPlayerId();
            gameService.endGame(game, winnerId);
        }
        return null;
    }

    private int isWin(String gameStatus, int index, char color) {
        int[] dx = {-1, 1, -1, 1, 0, 0, 1, -1}, dy = {0, 0, 1, -1, 1, -1, 1, -1};
        int row = index / 15;
        int col = index % 15;
        for (int i = 0; i < 8; i += 2) {
            int x = row + dx[i], y = col + dy[i];
            int cnt = 1;
            while (x >= 0 && x < 15 && y >= 0 && y < 15 && gameStatus.charAt(x * 15 + y) == color) {
                cnt++;
                x += dx[i];
                y += dy[i];
            }
            x = row + dx[i + 1];
            y = col + dy[i + 1];
            while (x >= 0 && x < 15 && y >= 0 && y < 15 && gameStatus.charAt(x * 15 + y) == color) {
                cnt++;
                x += dx[i + 1];
                y += dy[i + 1];
            }
            if (cnt == 5) {
                return 1;
            } else if (cnt > 5) {
                return 2;
            }
        }
        return 0;
    }

}
