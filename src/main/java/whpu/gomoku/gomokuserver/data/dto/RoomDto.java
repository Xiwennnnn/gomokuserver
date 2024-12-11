package whpu.gomoku.gomokuserver.data.dto;

import lombok.Data;
import whpu.gomoku.gomokuserver.GomokuContext;
import whpu.gomoku.gomokuserver.data.entity.Room;
import whpu.gomoku.gomokuserver.service.UserService;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoomDto implements Serializable {
    private Long id;
    private Long ownerId;
    private Long playerId;
    private Date createTime;
    private String ownerHandle;
    private String playerHandle;
    private Integer playerCount;
    private Boolean isPrivate;
    /**
     * 房间的状态：0-准备中，1-游戏中，2-已关闭
     */
    private Integer status;

    public static RoomDto fromDo(Room room) {
        UserService userService = GomokuContext.applicationContext.getBean(UserService.class);
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setOwnerId(room.getOwnerId());
        dto.setPlayerId(room.getPlayerId());
        dto.setCreateTime(room.getCreateTime());
        if (room.getOwnerId() != null) {
            dto.setOwnerHandle(userService.getUserById(room.getOwnerId()).getHandle());
        }
        if (room.getPlayerId() != null) {
            dto.setPlayerHandle(userService.getUserById(room.getPlayerId()).getHandle());
        }
        dto.setPlayerCount(room.getPlayerCount());
        dto.setStatus(room.getStatus());
        dto.setIsPrivate(room.getIsPrivate());
        return dto;
    }
}
