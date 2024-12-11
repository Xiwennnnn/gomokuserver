package whpu.gomoku.gomokuserver.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("game_moves")
@Data
public class GameMove {
    @TableId
    private Long id;
    private Long gameId;
    private Long playerId;
    private Integer moveNumber;
    private Integer movePosition;
    private Date moveTime;
    private Boolean isUndo;
}
