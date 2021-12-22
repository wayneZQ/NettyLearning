package com.polyu.zwq.buffer;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuffer {
    public static void main(String[] args) {
        /**
         * 创建一个bytebuffer
         * 该对象包含一个数组arr，是一个byte[10]
         * 在netty的buffer中不需要使用flip进行反转
         */

        ByteBuf buffer = Unpooled.buffer(10);
        for(int i=0;i<10;i++){
            buffer.writeByte(i);
        }
        System.out.println("capacity= "+buffer.capacity());

        for(int i=0;i<buffer.capacity();i++){
            System.out.println(buffer.getByte(i));
        }
    }


}
