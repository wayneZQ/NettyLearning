package com.polyu.zwq.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str="hello,Wayne";
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\java_Springboot_project\\test\\file01.txt");
        // 通过输出流获取文件channel
        FileChannel filechannel = fileOutputStream.getChannel();
        //每个channel对应一个缓冲，创建buffer
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

        //将str放入bytebuffer
        byteBuffer.put(str.getBytes());

        //对bytebuffer进行反转（此时position为最末）
        byteBuffer.flip();
        //将bytebuffer数据写入filechannel
        filechannel.write(byteBuffer);

        fileOutputStream.close();
    }
}
