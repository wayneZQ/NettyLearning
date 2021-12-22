package com.polyu.zwq.NettySample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     *当通道就绪就会触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client: "+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server!", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取事件时，事件驱动
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf= (ByteBuf) msg;
        System.out.println("Server reply: "+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("Server address is: "+ctx.channel().remoteAddress());
    }


    /**
     *异常处理
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
