package com.polyu.zwq.NettySample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         *  创建BossGroup和WorkerGroup
         *  1. 创建了两个线程组
         *  2.两个都是无限循环
         *  3.bossgroup 和 workergroup含有的子线程NioEventLoop个数默认是->cpu核数*2
         */
        EventLoopGroup BossGroup = new NioEventLoopGroup();
        NioEventLoopGroup WorkerGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程的方式进行设置
            //设置两个线程组
            bootstrap.group(BossGroup,WorkerGroup)
                    .channel(NioServerSocketChannel.class)    //使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)  //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler()); //将handler加入
                        }
                    });  //给workergroup的某一个eventloop 对应的管道设置处理器
            System.out.println("Server is ready.........");
            //启动服务器并绑定一个端口 并且同步处理
            ChannelFuture cf = bootstrap.bind(6667).sync();

            //给CF注册监听器，监控关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(cf.isSuccess()){
                        System.out.println("listen to port:6668 succeed!" );
                    }else {
                        System.out.println("listen to port:6668 failed!");
                    }
                }
            });

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            BossGroup.shutdownGracefully();
            WorkerGroup.shutdownGracefully();
        }




    }

}
