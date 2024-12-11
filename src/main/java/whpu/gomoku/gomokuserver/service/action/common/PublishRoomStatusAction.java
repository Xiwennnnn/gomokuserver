package whpu.gomoku.gomokuserver.service.action.common;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import whpu.gomoku.gomokuserver.GomokuContext;
import whpu.gomoku.gomokuserver.data.dto.RoomDto;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.data.response.data.GetRoomsData;

import java.util.List;

public class PublishRoomStatusAction {
    public static void publishRoomStatus(RoomDto room, Long userId) {
        Channel channel = GomokuContext.userChannelMap.get(userId);
        if (channel != null) {
            TextWebSocketFrame frame = new TextWebSocketFrame(
                    new GomokuResponse(ResponseActionType.UPDATE_ROOM_STATUS, room).toJsonString());
            channel.writeAndFlush(frame);
        }
    }

    public static void publishRoomUpdate(List<RoomDto> rooms) {
        String msg = new GomokuResponse(ResponseActionType.UPDATE_ROOMS, new GetRoomsData(rooms)).toJsonString();
        for (Channel channel : GomokuContext.userChannelMap.values()) {
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        }
    }
}
