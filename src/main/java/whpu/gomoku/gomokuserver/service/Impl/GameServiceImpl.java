package whpu.gomoku.gomokuserver.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.dto.GameMoveDto;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.entity.Game;
import whpu.gomoku.gomokuserver.data.entity.GameMove;
import whpu.gomoku.gomokuserver.data.mapper.GameMapper;
import whpu.gomoku.gomokuserver.data.mapper.GameMoveMapper;
import whpu.gomoku.gomokuserver.listener.event.EnterGameEvent;
import whpu.gomoku.gomokuserver.listener.event.GameOverEvent;
import whpu.gomoku.gomokuserver.service.GameService;
import whpu.gomoku.gomokuserver.service.action.common.PublicGameStatusAction;
import whpu.gomoku.gomokuserver.util.SnowflakeUtil;

import java.util.Date;

@Service
public class GameServiceImpl implements GameService {
    @Resource
    private GameMapper gameMapper;
    @Resource
    private GameMoveMapper gameMoveMapper;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public GameDto getGameById(Long gameId) {
        Game game = gameMapper.selectById(gameId);
        return game == null? null : GameDto.fromDo(game);
    }

    @Override
    public GameDto getGameByBlackId(Long blackId) {
        Game game = gameMapper.selectOne(Wrappers.<Game>lambdaQuery().eq(Game::getBlackPlayerId, blackId).eq(Game::getIsActive, true));
        return game == null? null : GameDto.fromDo(game);
    }

    @Override
    public GameDto getGameByWhiteId(Long whiteId) {
        Game game = gameMapper.selectOne(Wrappers.<Game>lambdaQuery().eq(Game::getWhitePlayerId, whiteId).eq(Game::getIsActive, true));
        return game == null? null : GameDto.fromDo(game);
    }

    @Override
    public void startGame(RoomDto room) {
        Game game = new Game();
        game.setId(SnowflakeUtil.generateId());
        game.setBlackPlayerId(room.getOwnerId());
        game.setWhitePlayerId(room.getPlayerId());
        game.setStartTime(new Date());
        game.setMoveCount(0);
        game.setIsActive(true);
        game.setGameFormat(0);
        String gameStatus = new String(new char[250]).replace("\0", "N");
        game.setGameStatus(gameStatus);
        gameMapper.insert(game);
        eventPublisher.publishEvent(new EnterGameEvent(room.getOwnerId()));
        eventPublisher.publishEvent(new EnterGameEvent(room.getPlayerId()));
        GameDto gameDto = GameDto.fromDo(game);
        PublicGameStatusAction.publicStartGame(gameDto);
    }

    @Override
    public void updateGame(GameDto gameDto) {
        Game game = new Game();
        game.setId(gameDto.getId());
        game.setGameStatus(new String(gameDto.getGameStatus()));
        game.setMoveCount(gameDto.getMoveCount());
        game.setBlackPlayerId(gameDto.getBlackPlayerId());
        game.setWhitePlayerId(gameDto.getWhitePlayerId());
        game.setStartTime(gameDto.getStartTime());
        game.setEndTime(gameDto.getEndTime());
        game.setWinnerId(gameDto.getWinnerId());
        game.setIsActive(gameDto.getIsActive());
        game.setGameFormat(gameDto.getGameFormat());
        gameMapper.updateById(game);
    }

    @Override
    public void gameMove(GameDto game, GameMoveDto gameMove) {
        updateGame(game);
        GameMove move = new GameMove();
        move.setId(SnowflakeUtil.generateId());
        move.setGameId(gameMove.getGameId());
        move.setMovePosition(gameMove.getMovePosition());
        move.setPlayerId(gameMove.getPlayerId());
        move.setMoveTime(new Date());
        move.setIsUndo(gameMove.getIsUndo());
        move.setMoveNumber(gameMove.getMoveNumber());
        gameMoveMapper.insert(move);
    }

    @Override
    public void undo(GameDto game) {
        GameMove lastMove = gameMoveMapper.selectOne(Wrappers.<GameMove>lambdaQuery()
                .eq(GameMove::getGameId, game.getId())
                .eq(GameMove::getIsUndo, false)
                .eq(GameMove::getMoveNumber, game.getMoveCount()));
        GameMove lastSecondMove = gameMoveMapper.selectOne(Wrappers.<GameMove>lambdaQuery()
                .eq(GameMove::getGameId, game.getId())
                .eq(GameMove::getIsUndo, false)
                .eq(GameMove::getMoveNumber, game.getMoveCount() - 1));
        if (lastMove == null || lastSecondMove == null) {
            return;
        }
        lastMove.setIsUndo(true);
        lastSecondMove.setIsUndo(true);
        gameMoveMapper.updateById(lastMove);
        gameMoveMapper.updateById(lastSecondMove);
        game.setMoveCount(Math.max(0, game.getMoveCount() - 2));
        game.getGameStatus().setCharAt(lastMove.getMovePosition(), 'N');
        game.getGameStatus().setCharAt(lastSecondMove.getMovePosition(), 'N');
        updateGame(game);
        PublicGameStatusAction.publicUndo(game);
    }

    @Override
    public void endGame(GameDto game, Long winnerId) {
        game.setIsActive(false);
        game.setEndTime(new Date());
        game.setWinnerId(winnerId);
        updateGame(game);
        eventPublisher.publishEvent(new GameOverEvent(winnerId, game.getId()));
    }

    @Override
    public void closeAllGame() {
        gameMapper.update(null, Wrappers.<Game>lambdaUpdate().set(Game::getIsActive, false));
    }
}
