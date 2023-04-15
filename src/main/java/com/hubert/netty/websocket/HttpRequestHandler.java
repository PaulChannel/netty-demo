package com.hubert.netty.websocket;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

/**
 * @author hustart1@126.com
 * @version 1.0.0
 * @ClassName HttpRequestHandler
 * @Description
 * @createTime 2023年04月15日 15:12:00
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


	// "/ws"
	private final String wsUri;

	private static final File INDEX;

	public HttpRequestHandler(String wsUri) {
		this.wsUri = wsUri;
	}

	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();

		try {
			String path = location.toURI() + "index.html";
			String file = path.contains("file:") ? path.substring(5) : path;

			INDEX = new File(file);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Unable to locate index.html", e);
		}

	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		boolean b = wsUri.equalsIgnoreCase(request.getUri());
		if (b) {

			// 如果请求了 WebSocket 则增加引用计数（调用retain， 并把他传递给下个Handler）
			ctx.fireChannelRead(request.retain());
		} else {
			if (HttpHeaders.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}
			RandomAccessFile file = new RandomAccessFile(INDEX, "r");

			HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(),
					HttpResponseStatus.OK);

			HttpHeaders header = response.headers().set(
					Names.CONTENT_TYPE,
					"text/plain; charset=UTF-8"
			);
			boolean keepAlive = HttpHeaders.isKeepAlive(request);
			if (keepAlive) {
				response.headers().set(Names.CONTENT_LENGTH, file.length());

				response.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
			}

			ctx.write(response);

			if(ctx.pipeline().get(SslHandler.class) == null) {
				ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
			} else {
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			// 如果没有请求保持Keep alive，关闭连接
			if (!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}


	}

	private void send100Continue(ChannelHandlerContext ctx) {
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}
}
