package com.polyu.zwq.BIO;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioSever {
    public static void main(String[] args) throws IOException {
        //1 创建线程池
        ExecutorService executorService= Executors.newCachedThreadPool();
        //2 若有客户端链接，创建线程通讯
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("server start!");
        while (true){
            //监听 等待客户端连接
            System.out.println("waiting for connection........");
            final Socket socket=serverSocket.accept();
            System.out.println("connect to one client");
            //创建一个线程与之通讯
            executorService.execute(new Runnable() {
                public void run() {
                    //在run中与客户端通讯
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void handler(Socket socket) throws IOException {
        try{
            System.out.println("线程信息 id="+ Thread.currentThread().getId() +"名字="+Thread.currentThread().getName());
            byte[] bytes = new byte[1024]; //接受数据
            InputStream inputStream = socket.getInputStream();
            while (true){
                System.out.println("线程信息 id="+ Thread.currentThread().getId() +"名字="+Thread.currentThread().getName());
                System.out.println("waiting for read.........."); //连接上之后没有数据会阻塞在此
                int read = inputStream.read(bytes);//将数据读入到byte数组中
                if(read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else{
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("close the connection");
            socket.close();
        }
    }
}
