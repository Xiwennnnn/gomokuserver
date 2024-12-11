package whpu.gomoku.gomokuserver.data.response.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReqUndoData extends BaseData implements Serializable {
    private final String msg = "request_undo";

}
