package com.polyu.zwq.InboundhandlerAndOutboundhandler.Client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("server's ip="+ctx.channel().remoteAddress());
        System.out.println("message from server: "+msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler send the message: ");
        //ctx.writeAndFlush(Unpooled.copiedBuffer())
        ctx.writeAndFlush(123456L);
    }
}
