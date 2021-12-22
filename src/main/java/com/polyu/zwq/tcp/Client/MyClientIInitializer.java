package com.polyu.zwq.tcp.Client;

import com.polyu.zwq.tcp.Server.MyByteToLongDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientIInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //自定义一个handler处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
