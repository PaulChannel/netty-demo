package com.yougen.netty.server.initiallizer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class IdleStateInitializer extends ChannelInitializer<Channel> {

    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS))
                .addLast("HeartIdleHandler", new HeartbeatHandler());
    }

    public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {


        private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("HeartHandler", CharsetUtil.UTF_8)
        );
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                ChannelFuture channelFuture = ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }


    }
}
