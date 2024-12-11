package whpu.gomoku.gomokuserver.data.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whpu.gomoku.gomokuserver.data.dto.GameDto;

@Data
@AllArgsConstructor
public class UndoData extends BaseData {
    private GameDto gameDto;
}
