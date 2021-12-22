package com.polyu.zwq.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupCharServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT=6667;

    public GroupCharServer(){
        try{
            //得到选择器
            selector=Selector.open();
            //初始化serversocketchannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置为非阻塞
            listenChannel.configureBlocking(false);
            //将该channel注册到selector上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 若有客户端连接，进行监听
     */
    public void listen(){
        try {
            //监听过程需要循环处理
            while (true){
                int count = selector.select();
                if(count > 0){ //count>0 说明有事件要进行处理
                    //遍历selectionkey集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while(keyIterator.hasNext()){
                        SelectionKey key = keyIterator.next();
                        //1 连接事件
                        if(key.isAcceptable()){
                            SocketChannel socketchannel = listenChannel.accept();
                            socketchannel.configureBlocking(false);
                            socketchannel.register(selector,SelectionKey.OP_READ);
                            //提示上线
                            System.out.println(socketchannel.getLocalAddress()+" online ");
                        }
                        //2 接收服务端的数据
                        if(key.isReadable()){ //通道发生read事件
                            // 处理读(专门方法)
                            readData(key);
                        }
                        //将当前的key删除
                        keyIterator.remove();
                    }
                }else{
                    System.out.println("waiting for client's message..........");
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 发生异常处理
        }
    }

    /**
     * 处理读数据,通过selectionkey反向获取到channel进行读取
     * @param key
     */
    private void readData(SelectionKey key)  {
        // 定义一个socketchannel
        SocketChannel channel=null;

        try{
            //取得关联的channel
            channel = (SocketChannel) key.channel();
            //创建缓冲
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int l = channel.read(byteBuffer);

            if(l>0){
                String msg = new String(byteBuffer.array());
                System.out.println("from client: "+ msg);

                //向其他客户端转发消息 专门写一个方法
                sendInfoToOther(msg,channel);
            }
        }catch (IOException e) {
           try {
               System.out.println(channel.getRemoteAddress()+ " offline....");
               //离线处理 取消注册 关闭通道
               key.cancel();
               channel.close();

           }catch (IOException ex){
               ex.printStackTrace();
           }
        }
    }

    /**
     * 将消息转发给其他客户端,需要排除发生消息的socketchannel
     * @param msg
     * @param self
     * @throws IOException
     */
    private void sendInfoToOther(String msg,SocketChannel self) throws IOException {
        System.out.println("server sends message to other....");
        // 遍历 所有注册到selector上的 并排除自己
        for(SelectionKey key:selector.keys()){
            // 通过key去除对应的socketchannel
            Channel TargetChannel = key.channel();
            //排除自己
            if(TargetChannel instanceof SocketChannel&& TargetChannel!=self){
                SocketChannel socketChannel= (SocketChannel) TargetChannel;
                // 将msg存储到buffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                socketChannel.write(byteBuffer);
            }
        }
    }
    public static void main(String[] args) {
        //创建一个服务器对象
        GroupCharServer charServer = new GroupCharServer();
        charServer.listen();

    }

}
