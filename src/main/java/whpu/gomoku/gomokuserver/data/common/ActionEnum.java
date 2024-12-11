package whpu.gomoku.gomokuserver.data.common;

public enum ActionEnum {
    GET_ROOM_LIST("get_room_list"),
    CREATE_ROOM("create_room"),
    JOIN_ROOM("join_room"),
    QUIT_ROOM("quit_room"),
    GAME_START("game_start"),
    MOVE("move"),
    UNDO("undo"),
    AC_UNDO("ac_undo"),
    AGREE_UNDO("agree_undo"),
    RESIGN("resign"),
    RESTART("restart"),
    LEAVE_ROOM("leave_room"),
    DROP_PIECE("drop_piece"),
    GIVE_UP("give_up"),
    OFFLINE("offline");

    public final String action;
    ActionEnum(String action) {
        this.action = action;
    }
}
