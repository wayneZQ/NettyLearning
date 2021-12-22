package com.polyu.zwq.HeartBeatTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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
                             *  加入一个netty提供的 idlestatehandler
                             *  1.空闲状态处理器
                             *  2.readerIdleTime->多长时间未读客户端发送的数据，发送一个心跳检测包检测是否连接状态
                             *  3.writerIdleTime->多长时间没有写，发送一个心跳检测包检测是否连接状态
                             *  4.allIdleTime->表示多长时间没有读写
                             *  5.当IdleStateEvent触发以后，会传递给管道的下一个handler去处理，通过回调userEventTrigger方法
                             */

                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            //加入一个空闲检测自定义handler
                            pipeline.addLast(new MyServerHandler());
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
