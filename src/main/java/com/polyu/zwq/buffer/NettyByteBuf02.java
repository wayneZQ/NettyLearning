package com.polyu.zwq.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 发送过程中使用bytebuffer发送
 */
public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建bytebuffer
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world", Charset.forName("utf-8"));

        //使用相关的方法
        if(byteBuf.hasArray()){ //
            byte[] content = byteBuf.array();
            //将content转成字符串
            System.out.println(new String(content,Charset.forName("utf-8")));
            System.out.println("bytebuffer="+byteBuf);
            System.out.println(byteBuf.readByte());  //读取出来的是ASCII码，会改变readindex
            System.out.println("len= "+byteBuf.readableBytes());
        }
    }

}
