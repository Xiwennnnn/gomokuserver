package whpu.gomoku.gomokuserver.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {
    @TableId
    private Long id;
    private String username;
    private String password;
    private String handle;
    private String role;
    private Integer winCount;
    private Integer totalCount;
    private String avatarUrl;
    /**
     * 0标识离线，1标识在线，2标识房间中，3标识游戏中
     */
    private Integer status;
}
