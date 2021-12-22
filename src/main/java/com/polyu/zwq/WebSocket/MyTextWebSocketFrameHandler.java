package com.polyu.zwq.WebSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * WebSocketFrame表示一个文本帧（frame）
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("server has receive the message: "+msg.text());

        //服务器回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("Server time:"+ LocalDateTime.now()+"---"+msg.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示唯一值 longtext是唯一的，shorttext不是唯一的
        System.out.println("handler is called:"+ctx.channel().id().asLongText());
        System.out.println("handler is called:"+ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemove is called:"+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exception! "+cause.getMessage());
        ctx.close();
    }
}
