package whpu.gomoku.gomokuserver.util;

import cn.hutool.core.util.IdUtil;

public class SnowflakeUtil {
    public static Long generateId() {
        return IdUtil.getSnowflakeNextId();
    }
}
