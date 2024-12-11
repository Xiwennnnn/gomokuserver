package whpu.gomoku.gomokuserver.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import whpu.gomoku.gomokuserver.data.common.GomokuConstant;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    private final Integer code;
    private String message;

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BusinessException(ErrorCode errorCode, String appendMessage) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage() + GomokuConstant.COMMA + appendMessage;
    }
}
