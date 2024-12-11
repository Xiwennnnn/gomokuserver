package whpu.gomoku.gomokuserver.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
@TableName("rooms")
public class Room {
    @TableId
    private Long id;
    private Long gameId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long ownerId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long playerId;
    private Integer playerCount;
    private Date createTime;
    /**
     * 房间的状态：0-准备中，1-游戏中，2-已关闭
     */
    private Integer status;
    private Boolean isPrivate;
}
