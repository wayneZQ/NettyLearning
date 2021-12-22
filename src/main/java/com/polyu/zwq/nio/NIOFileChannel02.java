package com.polyu.zwq.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {

        File file = new File("D:\\java_Springboot_project\\test\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //根据输入流对象获取相应的文件channel
        FileChannel channel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将channel数据读入到buffer中
        channel.read(byteBuffer);

        //将字节转为字符串
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
