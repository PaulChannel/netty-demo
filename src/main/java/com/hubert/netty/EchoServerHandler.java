package com.hubert.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import static io.netty.util.CharsetUtil.UTF_8;

/**
 * @author hustart1@126.com
 * @version 1.0.0
 * @ClassName EchoServerHandler
 * @Description
 * @createTime 2023年03月21日 20:51:00
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     接收消息处理函数
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        ByteBuf in = (ByteBuf) msg;
        System.out.println(
                "Service received :" + in.toString(UTF_8)
        );

        ctx.write(in);
    }
    /**
     * 消息处理完成函 数
      */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);

        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 发生异常处理函数
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
