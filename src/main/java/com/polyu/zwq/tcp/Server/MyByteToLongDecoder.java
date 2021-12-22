package com.polyu.zwq.tcp.Server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *decode会根据接收到的数据被调用多次，直到确定没有新元素被添加到list 或bytebuf没有更多可读字节
     * 如果list out不为空，就会将list传递给下一个channelinboundhandler处理
     * @param ctx 上下文对象
     * @param in  入站的ByteBuf
     * @param out  List集合， 将解码后的数据传给下一个decoder处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder is called!");
        //需要判断有8个字节才能读取
        if(in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
