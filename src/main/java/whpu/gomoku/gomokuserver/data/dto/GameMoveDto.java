package whpu.gomoku.gomokuserver.data.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import whpu.gomoku.gomokuserver.data.entity.GameMove;

@Data
public class GameMoveDto {
    private Long id;
    private Long gameId;
    private Long playerId;
    private Integer moveNumber;
    private Integer movePosition;
    private Boolean isUndo;

    public static GameMoveDto fromDo(GameMove gameMove) {
        GameMoveDto gameMoveDto = new GameMoveDto();
        gameMoveDto.setId(gameMove.getId());
        gameMoveDto.setGameId(gameMove.getGameId());
        gameMoveDto.setPlayerId(gameMove.getPlayerId());
        gameMoveDto.setMoveNumber(gameMove.getMoveNumber());
        gameMoveDto.setMovePosition(gameMove.getMovePosition());
        gameMoveDto.setIsUndo(gameMove.getIsUndo());
        return gameMoveDto;
    }
}
