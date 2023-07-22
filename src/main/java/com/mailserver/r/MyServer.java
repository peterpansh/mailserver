package com.mailserver.r;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(8852);
        System.out.println("Server 等待接收数据~");
        while(true) {
            Socket socket = serverSocket.accept();//这里来一个连接请求就让deal去处理，自己在这里等下一个连接请求
            deal(socket);
        }
    }
    
    public static void deal(Socket client){//这里创建了一个新的线程
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                DataInputStream dataInputStream=null;
                DataOutputStream dataOutputStream = null;
                String receiveFromClient="";
                try {
                    dataInputStream = new DataInputStream(client.getInputStream());
                    dataOutputStream = new DataOutputStream(client.getOutputStream());
                    //只是这个线程中一直while循环，不影响其他的线程。
                    while(true) {
                        receiveFromClient = dataInputStream.readUTF();
                        System.out.println("receive: "+receiveFromClient);
                        if(receiveFromClient.equals("bye"))
                        {
                            dataOutputStream.writeUTF("终于厌倦我了吗？客户端都是大猪蹄子！！");
                            dataOutputStream.flush();
                            break;
                        }
                        else {
                            dataOutputStream.writeUTF("你TM给我发 "+receiveFromClient+" 干什么？");
                            dataOutputStream.flush();
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        dataInputStream.close();
                        dataOutputStream.close();
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        }).start();
    }
}