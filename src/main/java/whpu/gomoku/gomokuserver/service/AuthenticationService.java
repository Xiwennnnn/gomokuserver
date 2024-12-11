package whpu.gomoku.gomokuserver.service;

import whpu.gomoku.gomokuserver.data.entity.User;
import whpu.gomoku.gomokuserver.exception.authen.ExistingUsernameException;

public interface AuthenticationService {
    public User authenticate(String username, String password);

    public User register(String username, String password) throws ExistingUsernameException;
}
