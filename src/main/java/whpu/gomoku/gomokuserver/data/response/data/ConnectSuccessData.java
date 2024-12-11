package whpu.gomoku.gomokuserver.data.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whpu.gomoku.gomokuserver.data.dto.UserDto;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectSuccessData extends BaseData implements Serializable {
    private UserDto user;
}
