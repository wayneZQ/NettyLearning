package com.polyu.zwq.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 使用transfer完成复制
 */
public class NIOFileChannel04 {
    public static void main(String[] args)throws Exception {
        // create stream
        FileInputStream fileInputStream = new FileInputStream("D:\\java_Springboot_project\\test\\a.png");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\java_Springboot_project\\test\\a2.png");
        // 获取各个filechannel

        FileChannel SourceChannel = fileInputStream.getChannel();
        FileChannel TargetChannel = fileOutputStream.getChannel();

        TargetChannel.transferFrom(SourceChannel,0,SourceChannel.size());

        SourceChannel.close();
        TargetChannel.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}
