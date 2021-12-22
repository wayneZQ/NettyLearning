package com.polyu.zwq.NIOpractice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        //得到一个socketchannel
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //提供服务器的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            //没有成功连接
            while (!socketChannel.finishConnect()){
                System.out.println("connection takes time,but it will not block");
            }
        }
        String str="hello.wayne's server!";
        //产生一个字节数组到buffer中,buffer的大小会与字节数组的大小保持一致
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据 将buffer中的数据写入到channel中
        socketChannel.write(byteBuffer);

        System.in.read();



    }
}
