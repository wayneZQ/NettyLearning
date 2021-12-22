package com.polyu.zwq.WebSocket;

import com.polyu.zwq.HeartBeatTest.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) //在bossgroup中增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            /**
                             * 基于http协议，使用http的编解码器
                             */
                            pipeline.addLast(new HttpServerCodec());
                            /**
                             * 是以块方式写的，添加chunkedWritehandler处理器
                             */
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合起来
                             * 当浏览器发送大量数据时就会发出多次http请求的原因
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * websocket，数据是以帧的形式传递（frame）
                             * websocketframe有六个子类
                             * WebSocketServerProtocolHandler核心功能，将http协议升级为ws协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            /**
                             * 自定义handler，处理业务逻辑
                             */
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
