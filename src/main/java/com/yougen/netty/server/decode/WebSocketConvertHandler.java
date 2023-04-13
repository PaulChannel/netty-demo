package com.yougen.netty.server.decode;

import java.util.List;

import com.yougen.netty.server.decode.WebSocketConvertHandler.WebSocketFrame.FrameType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.Data;


public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.WebSocketFrame> {


  protected void encode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> out) throws Exception {

    FrameType type = webSocketFrame.getType();

    ByteBuf payload = webSocketFrame.getData();
    switch (type) {
      case PING: {
        out.add(new PingWebSocketFrame(payload));
      }
      case PONG: {

      }
      case TEXT: {

      }
      case CLOSE: {

      }
      case BINARY: {

      }
      case CONTINUATION: {

      }
      default: {

      }
    }
  }

  protected void decode(ChannelHandlerContext channelHandlerContext, io.netty.handler.codec.http.websocketx.WebSocketFrame webSocketFrame,
      List<Object> list) throws Exception {

  }

  @Data
  @AllArgsConstructor
  public static final class WebSocketFrame {

    public enum FrameType {
      BINARY,
      CLOSE,
      PING,
      PONG,
      TEXT,
      CONTINUATION
    }

    private final FrameType type;
    private final ByteBuf data;


  }
}
