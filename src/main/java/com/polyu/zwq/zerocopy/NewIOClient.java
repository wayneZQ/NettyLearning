package com.polyu.zwq.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",7001));
        String filename="a.png";

        //得到一个文件channel
        FileChannel channel = new FileInputStream(filename).getChannel();

        //准备发送
        long startTime = System.currentTimeMillis();

        //在linux下 一次transferto就可以完成传输
        //在Windows下 一次transferto只能传输8m的文件
        long transferCount = channel.transferTo(0, channel.size(), socketChannel);// transferto底层调用了0拷贝
        System.out.println("total size is "+transferCount+" total time is "+ (System.currentTimeMillis()-startTime));
        channel.close();


    }
}
