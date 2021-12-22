package com.polyu.zwq.protocolTCP.Client;

import com.polyu.zwq.protocolTCP.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len=msg.getLength();
        byte[] content = msg.getContent();

        System.out.println("client received the message,the length is "+len);
        System.out.println("content is: "+new String(content,Charset.forName("utf-8")));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i < 10; i++){
            String msg="hello, server "+i+" ";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int length = msg.getBytes(Charset.forName("utf-8")).length;

            //创建一个协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLength(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);

        }
    }
}
