package whpu.gomoku.gomokuserver.data.dto;

import lombok.Data;
import whpu.gomoku.gomokuserver.data.entity.Game;

import java.util.Date;

@Data
public class GameDto {
    private Long id;
    private Long blackPlayerId;
    private Long whitePlayerId;
    private StringBuilder gameStatus;
    private Integer moveCount;
    private Long winnerId;
    private Date startTime;
    private Date endTime;
    private Integer gameFormat;
    private Boolean isActive;

    public static GameDto fromDo(Game game) {
        GameDto gameDto = new GameDto();
        gameDto.setId(game.getId());
        gameDto.setBlackPlayerId(game.getBlackPlayerId());
        gameDto.setWhitePlayerId(game.getWhitePlayerId());
        gameDto.setGameStatus(new StringBuilder(game.getGameStatus()));
        gameDto.setMoveCount(game.getMoveCount());
        gameDto.setWinnerId(game.getWinnerId());
        gameDto.setStartTime(game.getStartTime());
        gameDto.setEndTime(game.getEndTime());
        gameDto.setGameFormat(game.getGameFormat());
        gameDto.setIsActive(game.getIsActive());
        return gameDto;
    }
}
