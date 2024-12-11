package whpu.gomoku.gomokuserver.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("games")
public class Game {
    @TableId
    private Long id;
    private Long blackPlayerId;
    private Long whitePlayerId;
    private String gameStatus;
    private Integer moveCount;
    private Long winnerId;
    private Integer gameFormat;
    private Date startTime;
    private Date endTime;
    private Boolean isActive;
}
