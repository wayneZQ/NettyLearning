package com.polyu.zwq.GroupChatRoom;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor全局的事件执行器，一个单例
     */
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 使用一个hashmap管理用户
     */
    public static Map<String,Channel> channels= new HashMap<>();

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 表示连接建立，一旦连接上，第一个被执行
     * 将当前channel加入到 channelgroup
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户端的信息，推送给其他在线客户端,不需要遍历
        //该方法将channelgroup所有channel遍历，并发送消息
        channelGroup.writeAndFlush("[Client]"+channel.remoteAddress()+"enter the chat room!\n");
        channelGroup.add(channel);


    }


    /**
     * 表示channel处于活动状态，提示上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" online!");
    }

    /**
     * 表示channel处于不活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" offline!");
    }


    /**
     * 表示断开连接,将xx客户端离开信息推送给其他客户端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[client]"+channel.remoteAddress()+"leave the chat room!\n");
        System.out.println("channelgroup size is "+channelGroup.size());
    }

    /**
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        //遍历channelgroup，根据不同情况，回送不同的消息
        channelGroup.forEach(ch->{
            if(ch != channel){
                //若不是当前的channel，则直接转发
                ch.writeAndFlush("[client]"+channel.remoteAddress()+" says "+msg+"\n");
            }else{
                ch.writeAndFlush("[I say]"+msg+"\n");
            }
        });
    }

    /**
     * 发送异常，关闭通道即可
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
