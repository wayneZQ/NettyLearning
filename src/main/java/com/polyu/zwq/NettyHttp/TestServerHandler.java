package com.polyu.zwq.NettyHttp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 1. HttpObject 客户端和服务器端相互通信的数据 被封装为HttpObject
 */

public class TestServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg 是不是httprequest请求
        if(msg instanceof HttpRequest){
            System.out.println("msg type: "+ msg.getClass());
            System.out.println("client address: "+ ctx.channel().remoteAddress());

            /**
             * 进行资源过滤
             */
            // 获取msg
            HttpRequest httpRequest= (HttpRequest) msg;
            // 获取URI
            URI uri = new URI(httpRequest.uri());
            //uri的path就是图标
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("request for icon,ignore it!");
                return;
            }

            //回复信息给浏览器
            ByteBuf content= Unpooled.copiedBuffer("hello,I am server", CharsetUtil.UTF_8);

            //构造一个http响应
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            //设置文本类型
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            //设置
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //将构建好的response返回
            ctx.writeAndFlush(httpResponse);

        }

    }
}
