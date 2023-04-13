package com.yougen.netty.server.initiallizer;

import java.io.File;
import java.io.FileInputStream;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

  private final File file;
  private final SslContext context;

  public ChunkedWriteHandlerInitializer(File file, SslContext context) {
    this.file = file;
    this.context = context;
  }

  @Override
  protected void initChannel(Channel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline().addLast("sslHandler", new SslHandler(context.newEngine(channel.alloc())));
    pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
    pipeline.addLast("writeStreamHandler", new WriteStreamHandler());


  }

  public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
      ctx.writeAndFlush(new FileInputStream(file));
    }
  }
}
