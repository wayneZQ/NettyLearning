package com.polyu.zwq.NettyHttp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入netty提供的httpserverCodeC =》编解码器 coder and decoder
        //HttpServerCodec,基于http的编解码器
        pipeline.addLast("myHttpServerCodec",new HttpServerCodec());
        //增加一个自定义的handler
        pipeline.addLast("myTestServerHandler",new TestServerHandler());
        System.out.println("ok..........");
    }
}
