package whpu.gomoku.gomokuserver.data.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomSuccessData extends BaseData {
    private RoomDto room;
}
