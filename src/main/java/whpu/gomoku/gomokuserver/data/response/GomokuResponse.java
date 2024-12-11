package whpu.gomoku.gomokuserver.data.response;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Data;
import whpu.gomoku.gomokuserver.exception.BusinessException;
import whpu.gomoku.gomokuserver.exception.JsonException;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;
import whpu.gomoku.gomokuserver.util.JsonUtil;

import java.io.Serializable;

@Data
public class GomokuResponse implements Serializable {
    /**
     * 状态码
     */
    private Integer code = 200;
    private String action;
    /**
     * 响应对象
     */
    private Object data;

    public GomokuResponse(Integer code, String action, Object data) {
        this.code = code;
        this.action = action;
        this.data = data;
    }

    public GomokuResponse(BusinessException businessException) {
        this.code = businessException.getCode();
        this.action = "error";
        this.data = businessException.getMessage();
    }

    public  GomokuResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.action = "error";
        this.data = errorCode.getMessage();
    }

    public GomokuResponse(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public String toJsonString() throws JsonException {
        return JsonUtil.turnObjectToJson(this);
    }
}
