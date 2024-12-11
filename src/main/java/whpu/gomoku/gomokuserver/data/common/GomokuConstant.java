package whpu.gomoku.gomokuserver.data.common;

public final class GomokuConstant {
    // 系统常量
    public static final String COMMA = ",";

    public static final String JWT_ALGORITHM = "RS256"; // JWT加密算法
    public static final String JWT_PASSWORD = "anivia"; // JWT加密密码
    public static final String JWT_SALT = "8dfa22e9436b0388"; // JWT加密盐值
    public static final String JWT_KEY_INSTANCE = "RSA"; // JWT加密密钥实例类型
    public static final String JWT_PUBLIC_SECRET_KEY_POSITION = "key/public_key.pem";
    public static final String JWT_PRIVATE_SECRET_KEY_POSITION = "key/private_key.pem";

    public static final String SCHEMA_SQL_POSITION = "sql/schema.sql"; // 数据库建表语句位置

    public static final long JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7L; // JWT过期时间，一周
    public static final int HEARTBEAT_TIMEOUT = 30; // 心跳时间，单位秒
}
