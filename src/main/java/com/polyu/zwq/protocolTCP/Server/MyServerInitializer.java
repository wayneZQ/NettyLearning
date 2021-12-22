package com.polyu.zwq.protocolTCP.Server;

import com.polyu.zwq.protocolTCP.MyMessageDecoder;
import com.polyu.zwq.protocolTCP.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyMessageDecoder());
        pipeline.addLast(new MyMessageEncoder());

        //自定义handler处理业务
        pipeline.addLast(new MyServerHandler());

    }
}
