package com.polyu.zwq.InboundhandlerAndOutboundhandler.Server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("MyServerHandler is called!");
        System.out.println("Read From Client: "+ctx.channel().remoteAddress()+" Long: "+msg);

        //给客户端回送消息
        ctx.writeAndFlush(98765L);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
