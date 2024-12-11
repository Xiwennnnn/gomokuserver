package whpu.gomoku.gomokuserver.service.action.common;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import whpu.gomoku.gomokuserver.GomokuContext;
import whpu.gomoku.gomokuserver.data.dto.GameDto;
import whpu.gomoku.gomokuserver.data.dto.GameMoveDto;
import whpu.gomoku.gomokuserver.data.response.GomokuResponse;
import whpu.gomoku.gomokuserver.data.response.ResponseActionType;
import whpu.gomoku.gomokuserver.data.response.data.GameOverData;
import whpu.gomoku.gomokuserver.data.response.data.ReqUndoData;
import whpu.gomoku.gomokuserver.data.response.data.UndoData;

@Slf4j
public class PublicGameStatusAction {
    public static void publicStartGame(GameDto gameDto) {
        Long blackPlayerId = gameDto.getBlackPlayerId();
        Long whitePlayerId = gameDto.getWhitePlayerId();
        String msg = new GomokuResponse(ResponseActionType.GAME_START, gameDto).toJsonString();
        Channel blackPlayerChannel = GomokuContext.userChannelMap.get(blackPlayerId);
        if (blackPlayerChannel != null) {
            blackPlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
        }
        Channel whitePlayerChannel = GomokuContext.userChannelMap.get(whitePlayerId);
        if (whitePlayerChannel != null) {
            whitePlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
        }
    }

    public static void publicDropPiece(GameMoveDto gameMoveDto, GameDto gameDto) {
        Channel blackPlayerChannel = GomokuContext.userChannelMap.get(gameDto.getBlackPlayerId());
        Channel whitePlayerChannel = GomokuContext.userChannelMap.get(gameDto.getWhitePlayerId());
        String msg = new GomokuResponse(ResponseActionType.DROP_PIECE, gameMoveDto).toJsonString();
        blackPlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
        whitePlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public static void publicGameOver(GameDto game, String winnerHandle) {
        String msg = new GomokuResponse(ResponseActionType.GAME_OVER, new GameOverData(winnerHandle)).toJsonString();
        Channel blackPlayerChannel = GomokuContext.userChannelMap.get(game.getBlackPlayerId());
        Channel whitePlayerChannel = GomokuContext.userChannelMap.get(game.getWhitePlayerId());
        if (blackPlayerChannel != null) blackPlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
        if (whitePlayerChannel != null) whitePlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public static void publicUndo(GameDto game) {
        String msg = new GomokuResponse(ResponseActionType.UNDO, new UndoData(game)).toJsonString();
        Channel blackPlayerChannel = GomokuContext.userChannelMap.get(game.getBlackPlayerId());
        Channel whitePlayerChannel = GomokuContext.userChannelMap.get(game.getWhitePlayerId());
        blackPlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
        whitePlayerChannel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public static void publicRequestUndo(Long userId) {
        String msg = new GomokuResponse(ResponseActionType.REQUEST_UNDO, new ReqUndoData()).toJsonString();
        Channel userChannel = GomokuContext.userChannelMap.get(userId);
        userChannel.writeAndFlush(new TextWebSocketFrame(msg));
    }
}
