package com.polyu.zwq.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOserver {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(7001);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true){
            SocketChannel socketchannel = serverSocketChannel.accept();

            int readcount=0;
            while(readcount != -1){
                try {
                    readcount = socketchannel.read(byteBuffer);
                }catch (Exception e){
                    e.printStackTrace();
                }
                byteBuffer.rewind(); //倒带，将position=0，Mark作废
            }

        }
    }
}
