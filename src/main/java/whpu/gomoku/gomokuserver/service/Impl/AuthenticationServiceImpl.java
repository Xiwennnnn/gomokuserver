package whpu.gomoku.gomokuserver.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import whpu.gomoku.gomokuserver.data.common.RoleEnum;
import whpu.gomoku.gomokuserver.data.entity.User;
import whpu.gomoku.gomokuserver.data.mapper.UserMapper;
import whpu.gomoku.gomokuserver.exception.authen.ExistingUsernameException;
import whpu.gomoku.gomokuserver.service.AuthenticationService;
import whpu.gomoku.gomokuserver.util.SnowflakeUtil;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User authenticate(String username, String password) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    @Override
    public User register(String username, String password) throws ExistingUsernameException {
        if (userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username))!= null) {
            throw new ExistingUsernameException("用户名已存在");
        }
        User user = new User();
        user.setId(SnowflakeUtil.generateId());
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(RoleEnum.ROLE_USER);
        user.setHandle(username);
        int flag = userMapper.insert(user);
        return flag > 0 ? user : null;
    }
}
