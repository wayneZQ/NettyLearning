package com.polyu.zwq.NettySample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


/**
 * 1. 我们自定义一个handler需要继承netty规定好的某一个handler适配器（adapter）
 * 2. channelRead 读取数据的实现（这里我们可以读取客户端发送的消息）
 */

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * channelHandlercontext ctx:上下文对象，含有管道pipeline，通道channel，地址
     * msg 客户端发送的数据 默认object的形式
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //有一个非常耗时间的业务->异步执行->提交到该channel对应的NIOeventLOOP的taskqueue中

        //solution 1.用户程序自定义普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client!->2",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("发送异常: "+e.getMessage());
                }

            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client!->3",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("发送异常: "+e.getMessage());
                }

            }
        });

        //2. 用户自定义定时任务
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client!->4",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("发送异常: "+e.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);

//        Thread.sleep(10000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client!->hahahahaha!",CharsetUtil.UTF_8));
        System.out.println("go on..........");

//        System.out.println("server ctx= "+ ctx);
//        //将msg转成bytebuffer
//        //netty 提供的bytebuffer
//        ByteBuf byteBuffer=(ByteBuf) msg;
//        System.out.println("message from client: "+((ByteBuf) msg).toString(CharsetUtil.UTF_8));
//        System.out.println("client address is "+ ctx.channel().remoteAddress());

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入缓冲，并刷新到channel中
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client!->1",CharsetUtil.UTF_8));
    }

    /**
     * 异常处理，发生异常关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }
}
