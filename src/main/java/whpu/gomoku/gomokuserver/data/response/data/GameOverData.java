package whpu.gomoku.gomokuserver.data.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
    @AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameOverData extends BaseData implements Serializable {
    private String winner;
}
