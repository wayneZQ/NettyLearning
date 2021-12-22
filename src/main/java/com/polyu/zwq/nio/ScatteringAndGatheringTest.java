package com.polyu.zwq.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        // 使用ServerSocketChannel 和 socketchannel网络
        // 服务器使用buffer数组来读取
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] =ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);

        //等待客户端链接
        SocketChannel socketChannel=serverSocketChannel.accept();
        int messageLength = 8;
        //循环读取
        while (true){
            int byteRead=0; //读取到的字节数
            while(byteRead < messageLength){
                long r = socketChannel.read(byteBuffers);
                byteRead += r; //累计
                System.out.println("byteread=" + byteRead);
                //输出 流打印
                Arrays.asList(byteBuffers).stream().map(buffer ->"position="+buffer.position()+" "+"limit="+ buffer.limit()).forEach(System.out::println);
            }
            //将所有buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer->buffer.flip());

            //将数据读出显示到客户端
            long byteWrite=0;
            while(byteWrite<messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            Arrays.asList(byteBuffers).forEach(buffer-> {buffer.clear();});
            System.out.println("byteread="+ byteRead+" "+"bytewrite="+byteWrite);

        }
    }
}
