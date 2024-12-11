package whpu.gomoku.gomokuserver.data.response;

public final class ResponseActionType {
    public static final String USER_INFO = "user_info";
    public static final String LOGIN_SUCCESS = "login_success";
    public static final String UPDATE_ROOMS = "update_rooms";
    public static final String CREATE_ROOM_SUCCESS = "create_room_success";
    public static final String JOIN_ROOM_SUCCESS = "join_room_success";
    public static final String UPDATE_ROOM_STATUS = "update_room_status";
    public static final String QUIT_ROOM_SUCCESS = "quit_room_success";
    public static final String GAME_START = "game_start";
    public static final String DROP_PIECE = "drop_piece";
    public static final String UNDO = "undo";
    public static final String REQUEST_UNDO = "request_undo";
    public static final String GAME_OVER = "game_over";
}
