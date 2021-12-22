package com.polyu.zwq.NIOpractice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建serversocketchannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到一个selector对象
        Selector selector = Selector.open();

        // 绑定一个端口 在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把 serversocketchannel注册到selector上，关心的事件为 op_accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true){
            //阻塞1秒
            if(selector.select(1000) == 0){ //没有任何事件发送
                System.out.println("server has waited for 1 second,but no connection,so continue...");
                continue;
            }
            //如果返回非0 有事件发生,就可以获取selectionkey集合
            // 通过selectionkey反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历集合
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                // 获取到selectionkey
                SelectionKey key = keyIterator.next();
                //根据不同的事件做相应的处理

                //1 连接事件
                if(key.isAcceptable()){ //有新客户端连接，产生一个新socketchannel
                    //通过事件驱动，避免了原先方法的阻塞
                    SocketChannel socketchannel = serverSocketChannel.accept();//因为已经有客户端请求连接，不需要等待，不会发生阻塞
                    socketchannel.configureBlocking(false);
                    //将当前的socketcha注册到selector上,关注事件为read，同时给socketchannel绑定一个buffer
                    socketchannel.register(selector, SelectionKey.OP_READ,ByteBuffer.allocate(1024));
                }
                //2 读事件
                if(key.isReadable()){ //发生op_read
                    // 通过key 反向获取到对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到channel关联到buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    //将channel中的数据读到buffer中
                    channel.read(buffer);
                    System.out.println("read from client:"+ new String(buffer.array()));
                }
                //手动从集合中移除本次的selectionkey
                keyIterator.remove();

            }

        }

    }
}
