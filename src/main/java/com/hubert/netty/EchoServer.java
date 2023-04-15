package com.hubert.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * @author hustart1@126.com
 * @version 1.0.0
 * @ClassName EchoServer
 * @Description
 * @createTime 2023年03月23日 17:03:00
 */
public class EchoServer {


	final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException {
		//传入的端口参数不正确
		if (args.length != 1) {
			System.err.println("Usage: " + EchoServer.class.getSimpleName());
		}
		int port = Integer.parseInt(args[0]);
		EchoServer echoServer = new EchoServer(port);
    // 启动服务器
		echoServer.start();
	}

	public void start() throws InterruptedException {
	    // 新建事件处理器
		final EchoServerHandler echoServerHandler = new EchoServerHandler();
		// 时
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(group)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(echoServerHandler);
						}
					});

			ChannelFuture future = serverBootstrap.bind().sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully().sync();
		}

	}
}
