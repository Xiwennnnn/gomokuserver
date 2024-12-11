package whpu.gomoku.gomokuserver.exception.common;

import whpu.gomoku.gomokuserver.exception.BusinessException;

public enum ErrorCode {
    // 常规异常 10 - 199
    AUTHENTICATION_ERROR(10, "用户名或密码错误"),
    USERNAME_EXIST_ERROR(11, "用户名已存在"),
    USER_ALREADY_IN_ROOM(12, "用户已在房间中"),
    ROOM_ALREADY_FULL(13, "房间已满"),
    ROOM_NOT_EXIST_ERROR(14, "房间不存在"),
    ROOM_ALREADY_STARTED(15, "房间已开始"),
    USER_NOT_IN_ROOM(16, "用户不在房间中"),

    // 业务异常 201 - 999
    JSON_MATCH_ERROR(201, "Json中的行为与业务行为不匹配"),
    JSON_PARSE_ERROR(202, "Json内容解析失败"),
    REQUEST_FORMAT_ERROR(203, "请求格式错误"),
    JSON_FORMAT_ERROR(204, "Json格式错误"),
    ACTION_FAILED_ERROR(205, "操作失败"),
    USER_NOT_LOGIN_ERROR(401, "用户未登录"),
    TOKEN_EXPIRED_ERROR(402, "Token已过期"),
    TOKEN_INVALID_ERROR(403, "Token无效"),

    // 系统异常 1000 - 1999
    SERVER_HAS_CLOSED(1000, "服务器已关闭");



    private final Integer code;
    private final String action;
    private final String message;
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.action = "error";
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }

    public BusinessException getException() {
        return new BusinessException(this);
    }

    public String getExceptionMessage() {
        return String.format("[%d] %s", code, message);
    }
}
