package com.polyu.zwq.protocolTCP;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder  extends ReplayingDecoder<MessageProtocol> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(" MyMessageDecoder decode is called!");
        //需要将得到的二进制字节码转为messageprotocol数据包（转为对象）
        int length = in.readInt();
        //按照长度得到字节数组
        byte[] content = new byte[length];
        in.readBytes(content);

        //封装成messageprotocol对象，放入out中，传递给下一个handler进行业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLength(length);
        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}
