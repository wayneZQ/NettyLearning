package com.polyu.zwq.protocolTCP;

import com.polyu.zwq.protocolTCP.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class MyMessageEncoder  extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MyMessageEncoder encode is called!");
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
