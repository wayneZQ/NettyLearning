package com.polyu.zwq.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;


    public GroupChatClient() throws IOException {
        //完成初始化工作
        selector = Selector.open();
        //连接服务器
        socketChannel=socketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        //将channel注册到selector上
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " client is ready!");
    }


    /**
     * 向服务器发送消息
     *
     * @param info
     */
    public void sendInfo(String info) {

        info = username + " send: " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器接收消息
     *
     * @param args
     */
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) { //即有可用的通道
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //如果key是可读的
                    if (key.isReadable()) {
                        //得到相关的channel
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        //得到buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        //将buffer中的数据转为字符串
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());
                    }
                }
                //删除当前的selectorkey 防止重复操作
                iterator.remove();
            } else {
                System.out.println("no usable channel........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程,监听消息
        new Thread() {
            public void run() {
                while (true) {
                    chatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(1000); //休眠3秒，每隔三秒读取一次数据
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
