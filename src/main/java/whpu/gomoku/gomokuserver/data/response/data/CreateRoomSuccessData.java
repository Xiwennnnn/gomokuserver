package whpu.gomoku.gomokuserver.data.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomSuccessData extends BaseData implements Serializable {
    private RoomDto room;
}
