package whpu.gomoku.gomokuserver.data.dto;

import lombok.Data;
import whpu.gomoku.gomokuserver.data.entity.User;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String handle;
    private String role;
    private Integer winCount;
    private Integer totalCount;
    private String avatarUrl;
    /**
     * 0标识离线，1标识在线，2标识房间中，3标识游戏中
     */
    private Integer status;

    public static UserDto fromDo(User userDo) {
        UserDto userDto = new UserDto();
        userDto.setId(userDo.getId());
        userDto.setUsername(userDo.getUsername());
        userDto.setHandle(userDo.getHandle());
        userDto.setRole(userDo.getRole());
        userDto.setWinCount(userDo.getWinCount());
        userDto.setTotalCount(userDo.getTotalCount());
        userDto.setAvatarUrl(userDo.getAvatarUrl());
        userDto.setStatus(userDo.getStatus());
        return userDto;
    }
}
