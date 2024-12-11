package whpu.gomoku.gomokuserver.data.event;

import java.util.Date;

public abstract class AbstractRequest {
    protected Date time = new Date();
    public abstract String getActionType();
}
