package com.hubert.netty.oio;

import java.net.InetSocketAddress;
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
import io.netty.channel.socket.oio.OioServerSocketChannel;

/**
 * @author hustart1@126.com
 * @version 1.0.0
 * @ClassName NettyOioServer
 * @Description
 * @createTime 2023年04月03日 22:43:00
 */
public class NettyOioServer {

	public void server(int port) throws Exception {
		ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi", Charset.forName("UTF-8")));
		EventLoopGroup group = new NioEventLoopGroup();

		ServerBootstrap bootstrap  = new ServerBootstrap();

		bootstrap.group(group).channel(OioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								super.channelActive(ctx);
								ChannelFuture future = ctx.writeAndFlush(byteBuf.duplicate());
								future.addListener(ChannelFutureListener.CLOSE);
							}
						});
					}
				});
		ChannelFuture future = bootstrap.bind().sync();
		future.channel().closeFuture().sync();
		group.shutdownGracefully();


	}
}
