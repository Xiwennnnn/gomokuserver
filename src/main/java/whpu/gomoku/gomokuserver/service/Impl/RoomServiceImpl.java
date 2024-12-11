package whpu.gomoku.gomokuserver.service.Impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.entity.Room;
import whpu.gomoku.gomokuserver.data.mapper.RoomMapper;
import whpu.gomoku.gomokuserver.listener.event.QuitRoomEvent;
import whpu.gomoku.gomokuserver.service.RoomService;
import whpu.gomoku.gomokuserver.service.action.common.PublishRoomStatusAction;
import whpu.gomoku.gomokuserver.util.SnowflakeUtil;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<RoomDto> getAliveRoomList() {
        return roomMapper.
                selectList(Wrappers.<Room>lambdaQuery().ne(Room::getStatus, 2))
                .stream()
                .filter(room -> !room.getIsPrivate())
                .map(RoomDto::fromDo)
                .toList();
    }

    @Override
    public RoomDto createRoom(UserDto user) {
        Room room = new Room();
        room.setCreateTime(DateUtil.date());
        room.setId(SnowflakeUtil.generateId());
        room.setOwnerId(user.getId());
        room.setPlayerCount(1);
        room.setStatus(0);
        room.setIsPrivate(false);
        roomMapper.insert(room);
        return RoomDto.fromDo(room);
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) return null;
        return RoomDto.fromDo(room);
    }
    @Override
    public RoomDto joinRoom(UserDto user, Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) return null;
        room.setPlayerCount(room.getPlayerCount() + 1);
        room.setPlayerId(user.getId());
        roomMapper.updateById(room);
        return RoomDto.fromDo(room);
    }

    @Override
    public RoomDto quitRoom(Long userId, Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) return null;
        room.setPlayerCount(room.getPlayerCount() - 1);
        if (room.getPlayerCount() <= 0) {
            room.setStatus(2);
            deleteRoomById(roomId);
        } else {
            if (userId.equals(room.getOwnerId())) {
                room.setOwnerId(room.getPlayerId());
            }
            room.setPlayerId(null);
            roomMapper.updateById(room);
            PublishRoomStatusAction.publishRoomStatus(RoomDto.fromDo(room), room.getOwnerId());
        }
        eventPublisher.publishEvent(new QuitRoomEvent(userId, roomId));
        return RoomDto.fromDo(room);
    }

    @Override
    public RoomDto getRoomByOwner(Long ownerId) {
        Room room = roomMapper.selectOne(Wrappers.<Room>lambdaQuery()
                .eq(Room::getOwnerId, ownerId));
        if (room == null) return null;
        return RoomDto.fromDo(room);
    }

    @Override
    public Integer deleteRoomById(Long roomId) {
        return roomMapper.deleteById(roomId);
    }

    @Override
    public RoomDto getRoomByParticipant(Long participantId) {
        Room room = roomMapper.selectOne(Wrappers.<Room>lambdaQuery()
               .eq(Room::getPlayerId, participantId));
        if (room == null) return null;
        return RoomDto.fromDo(room);
    }

    @Override
    public void exitRoom(UserDto user) {
        RoomDto room = getRoomByOwner(user.getId());
        if (room != null) {
            quitRoom(user.getId(), room.getId());
        }
        room = getRoomByParticipant(user.getId());
        if (room!= null) {
            quitRoom(user.getId(), room.getId());
        }
    }

    @Override
    public void closeAllRooms() {
        roomMapper.delete(Wrappers.emptyWrapper());
    }
}
