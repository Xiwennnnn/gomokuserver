package whpu.gomoku.gomokuserver.service;

import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.dto.GameMoveDto;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;

public interface GameService {
    public GameDto getGameById(Long gameId);
    public GameDto getGameByBlackId(Long blackId);
    public GameDto getGameByWhiteId(Long whiteId);
    public void startGame(RoomDto room);
    public void updateGame(GameDto game);
    public void gameMove(GameDto game, GameMoveDto gameMove);
    public void endGame(GameDto game, Long winnerId);
    public void undo(GameDto game);
    public void closeAllGame();
}
