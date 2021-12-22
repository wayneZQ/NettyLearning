package com.polyu.zwq.protocolTCP.Client;

import com.polyu.zwq.protocolTCP.MyMessageDecoder;
import com.polyu.zwq.protocolTCP.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientIInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //编码器
        pipeline.addLast(new MyMessageEncoder());
        pipeline.addLast(new MyMessageDecoder());

        //自定义一个handler处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
