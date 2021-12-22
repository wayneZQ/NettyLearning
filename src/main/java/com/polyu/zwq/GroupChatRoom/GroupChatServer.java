package com.polyu.zwq.GroupChatRoom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {
    private int port;


    public GroupChatServer(int port){
        this.port=port;
    }

    /**
     * 处理客户端请求
     */
    public void  run() throws InterruptedException {
        //创建两个线程组
        NioEventLoopGroup BossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup WorkerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(BossGroup,WorkerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128) //设置等待队列最大连接数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            //向pipeline加入处理器 解码器,编码器，自己的handler
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new GroupChatServerHandler());

                        }
                    });
            System.out.println("netty server is ready.........");
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        }finally {
            BossGroup.shutdownGracefully();
            WorkerGroup.shutdownGracefully();
        }


    }
    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(7000).run();
    }
}
