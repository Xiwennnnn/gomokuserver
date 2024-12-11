package whpu.gomoku.gomokuserver.data.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginSuccessData extends BaseData implements Serializable {
    private String token;
}
