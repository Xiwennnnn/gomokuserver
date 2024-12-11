package whpu.gomoku.gomokuserver.service;

import whpu.gomoku.gomokuserver.data.dto.UserDto;

public interface UserService {
    public Integer setOline(Long id);
    public Integer setOffline(Long id);
    public Integer setInGame(Long id);
    public Integer setInRoom(Long id);
    public Integer getStatus(Long id);

    public UserDto getUserById(Long id);
    public UserDto getUserByUsername(String username);
}
