package com.yougen.netty.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class PlainNioServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocketChannel serverSocketChannel =  ServerSocketChannel.open();

    serverSocketChannel.configureBlocking(false);

    final ByteBuf bf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi", Charset.forName("UTF-8")));

    EventLoopGroup group = new NioEventLoopGroup();


    try {

      ServerBootstrap bootstrap = new ServerBootstrap();

      bootstrap.group(group)
          .channel(NioServerSocketChannel.class)
          .localAddress(new InetSocketAddress(9000))
          .childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                  ctx.writeAndFlush(bf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                }
              });
            }
          });
      ChannelFuture sync = bootstrap.bind().sync();
      sync.channel().closeFuture().sync();

    } catch (Exception e) {

    } finally {
      group.shutdownGracefully().sync();
    }

  }
}
