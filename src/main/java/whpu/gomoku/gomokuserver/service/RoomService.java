package whpu.gomoku.gomokuserver.service;

import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.dto.UserDto;

import java.util.List;

public interface RoomService {
    public List<RoomDto> getAliveRoomList();
    public RoomDto createRoom(UserDto user);
    public RoomDto getRoomById(Long roomId);
    public RoomDto joinRoom(UserDto user, Long roomId);
    public RoomDto quitRoom(Long userId, Long roomId);
    public RoomDto getRoomByOwner(Long ownerId);
    public RoomDto getRoomByParticipant(Long participantId);
    public Integer deleteRoomById(Long roomId);
    public void exitRoom(UserDto user);
    public void closeAllRooms();
}
