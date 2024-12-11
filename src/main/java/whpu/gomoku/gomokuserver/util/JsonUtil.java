package whpu.gomoku.gomokuserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import whpu.gomoku.gomokuserver.exception.JsonException;
import whpu.gomoku.gomokuserver.exception.common.ErrorCode;

public class JsonUtil {
    private static final ThreadLocal<ObjectMapper> objMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    public static String turnObjectToJson(Object obj) throws JsonException {
        try {
            return objMapperThreadLocal.get().writeValueAsString(obj);
        } catch (Exception e) {
            throw new JsonException(ErrorCode.JSON_PARSE_ERROR.getMessage(), e);
        }
    }
}
