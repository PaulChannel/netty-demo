package com.yougen.netty.server.ssl;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;

public class SslChannelInitializer extends ChannelInitializer<Channel> {

  private final SslContext context;

  private final boolean startTls;

  public SslChannelInitializer(SslContext context, boolean startTls) {
    this.context = context;
    this.startTls = startTls;
  }

  protected void initChannel(Channel channel) throws Exception {
    SSLEngine sslEngine = context.newEngine(channel.alloc());
    sslEngine.setUseClientMode(false);

    channel.pipeline().addFirst("ssl", new SslChannelInitializer(context, false));

  }
}
