package whpu.gomoku.gomokuserver.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import whpu.gomoku.gomokuserver.data.dto.UserDto;
import whpu.gomoku.gomokuserver.data.entity.User;
import whpu.gomoku.gomokuserver.data.mapper.UserMapper;
import whpu.gomoku.gomokuserver.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Integer setOline(Long id) {
        return userMapper.update(
                Wrappers.<User>lambdaUpdate().eq(User::getId, id).set(User::getStatus, 1));
    }

    @Override
    public Integer setOffline(Long id) {
        return userMapper.update(
                Wrappers.<User>lambdaUpdate().eq(User::getId, id).set(User::getStatus, 0));
    }

    @Override
    public Integer setInGame(Long id) {
        return userMapper.update(
                Wrappers.<User>lambdaUpdate().eq(User::getId, id).set(User::getStatus, 3));
    }

    @Override
    public Integer setInRoom(Long id) {
        return userMapper.update(
                Wrappers.<User>lambdaUpdate().eq(User::getId, id).set(User::getStatus, 2));
    }

    @Override
    public Integer getStatus(Long id) {
        return userMapper.selectOne(
                Wrappers.<User>lambdaQuery().eq(User::getId, id))
                .getStatus();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        return UserDto.fromDo(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return UserDto.fromDo(userMapper.selectOne(
                Wrappers.<User>lambdaQuery().eq(User::getUsername, username)));
    }
}
