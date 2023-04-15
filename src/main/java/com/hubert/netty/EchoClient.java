package com.hubert.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * @author hustart1@126.com
 * @version 1.0.0
 * @ClassName EchoClient
 * @Description
 * @createTime 2023年04月03日 21:23:00
 */
public class EchoClient {

	private String host;
	private Integer port;

	public EchoClient() {}

	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(group)
				.remoteAddress(host, port)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline().addLast(new EchoClientHandler());
					}
				});
		group.shutdownGracefully().sync();


	}

	public static void main(String[] args) throws Exception {
		EchoClient echoClient = new EchoClient();
		if (args.length != 2) {
			System.err.println("Usage: " + EchoClient.class.getSimpleName() +
					" <host> <port>");
		}
		echoClient.host = args[0];
		echoClient.port = Integer.parseInt(args[1]);
		echoClient.start();
	}
}
