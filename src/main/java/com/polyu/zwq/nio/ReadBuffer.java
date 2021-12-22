package com.polyu.zwq.nio;

import java.nio.ByteBuffer;

/**
 * 可以将buffer设置为只读属性
 */
public class ReadBuffer {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for(int i=0;i<64;i++){
            byteBuffer.put((byte) i);
        }
        byteBuffer.flip();
        //得到一个只读buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer.getClass());
    }
}
