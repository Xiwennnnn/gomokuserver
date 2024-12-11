package whpu.gomoku.gomokuserver.data.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRoomsData extends BaseData implements Serializable {
    private List<RoomDto> rooms;
}
