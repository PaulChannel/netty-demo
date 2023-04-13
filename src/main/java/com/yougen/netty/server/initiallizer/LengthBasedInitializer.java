package com.yougen.netty.server.initiallizer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class LengthBasedInitializer extends ChannelInitializer<Channel> {

  @Override
  protected void initChannel(Channel channel) throws Exception {
    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(64 * 8, 0, 8));

  }
}
