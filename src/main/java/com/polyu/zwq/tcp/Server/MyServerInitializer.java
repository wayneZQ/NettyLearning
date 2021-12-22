package com.polyu.zwq.tcp.Server;

import com.polyu.zwq.tcp.Client.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //自定义handler处理业务
        pipeline.addLast(new MyServerHandler());

    }
}
