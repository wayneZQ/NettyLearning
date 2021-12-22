package com.polyu.zwq.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class MapByteBufferTest {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\java_Springboot_project\\test\\1.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        //读写模式 起始位置 映射到内存的大小（将文件的多少个字节映射到内存）
        MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mapBuffer.put(0,(byte)'B');
        mapBuffer.put(3, (byte) 'V');

        randomAccessFile.close();
    }
}
