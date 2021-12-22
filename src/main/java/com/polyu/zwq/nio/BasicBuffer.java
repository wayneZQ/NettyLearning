package com.polyu.zwq.nio;

import java.nio.IntBuffer;

/**
 * buffer使用
 */
public class BasicBuffer {
    public static void main(String[] args) {
        // 大小为5，可以存放五个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向buffer存放数据
        for (int i=0;i<intBuffer.capacity();i++){
            intBuffer.put(i+10);
        }

        //从buffer中读取数据
        //将buffer读写切换
        intBuffer.flip();

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
