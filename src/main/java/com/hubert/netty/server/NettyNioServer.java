package com.hubert.netty.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;

/**
 * @author hustart1@126.com
 * @version 1.0.0
 * @ClassName NettyNioServer
 * @Description
 * @createTime 2023年04月06日 19:51:00
 */
public class NettyNioServer {

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();

		ByteBuffer buffer = ByteBuffer.wrap("Hi".getBytes());
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			InetSocketAddress localAddress = new InetSocketAddress(9000);
			ServerBootstrap serverBootstrap = bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(localAddress);
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							super.channelActive(ctx);
							ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE).sync();
						}
					});
					ChannelFuture sync = bootstrap.bind().sync();
					sync.channel().close().sync();
				}


			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Future<?> future = group.shutdownGracefully();
		}


	}
}
