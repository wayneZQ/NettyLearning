package com.polyu.zwq.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("D:\\java_Springboot_project\\test\\1.txt");
        FileChannel fileChannel_1 = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\java_Springboot_project\\test\\2.txt");
        FileChannel fileChannel_2 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while(true){
            //重置标志位 position limit mark
            byteBuffer.clear();

            //从通道读取数据放到缓冲区
            int read = fileChannel_1.read(byteBuffer);
            if(read == -1){ //read finish
                break;
            }
            //将buffer中数据写入到filechannel2
            byteBuffer.flip();
            fileChannel_2.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
