package com.yougen.netty.server.decode;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ToIntegerDecoder extends ByteToMessageDecoder {

  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
    if(byteBuf.readableBytes() >= 4) {
      list.add(byteBuf.readInt());
    }
  }
}
