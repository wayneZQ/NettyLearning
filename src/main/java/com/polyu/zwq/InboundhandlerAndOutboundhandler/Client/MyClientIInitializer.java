package com.polyu.zwq.InboundhandlerAndOutboundhandler.Client;

import com.polyu.zwq.InboundhandlerAndOutboundhandler.Server.MyByteToLongDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientIInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个出站的handler 对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());
        //入站的handler对数据进行解码
        pipeline.addLast(new MyByteToLongDecoder());
        //自定义一个handler处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
