package com.mailserver.r;
 

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {
public static void main(String[] args) {
try {
ServerSocket serverSocket =new ServerSocket(9999);
                Socket client = serverSocket.accept();
                InputStream inputStream = client.getInputStream();
                DataInputStream dataInputStream =new DataInputStream(inputStream);
                while (true){
//                    byte b = dataInputStream.readByte();
//                    int len = dataInputStream.readInt();
//                    byte[] data =new byte[len -5];
//                    dataInputStream.readFully(data);
//                    String str =new String(data);
//                    System.out.println("获取的数据类型为："+b);
//                    System.out.println("获取的数据长度为："+len);
//                    System.out.println("获取的数据内容为："+str);
                	
                	 String  receiveFromClient = dataInputStream.readUTF();
                     System.out.println("receive: "+receiveFromClient);
                     
                }
}catch (IOException e) {
e.printStackTrace();
        }
}
}

