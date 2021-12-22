package com.polyu.zwq.protocolTCP.Server;

import com.polyu.zwq.protocolTCP.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据并处理
        int length = msg.getLength();
        byte[] content = msg.getContent();

        System.out.println("server get the message as followed ");
        System.out.println("length= "+length);
        System.out.println("content: "+new String(content,Charset.forName("utf-8")));
        System.out.println("No."+(++this.count)+" message");

        //回复消息
        String responseContent=UUID.randomUUID().toString();
        int responseLength=responseContent.getBytes("utf-8").length;
        byte[] responseContentBytes = responseContent.getBytes("utf-8");

        //构建一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLength(responseLength);
        messageProtocol.setContent(responseContentBytes);

        ctx.writeAndFlush(messageProtocol);

    }
}
