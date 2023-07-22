package com.mailserver.r;

 
 

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocketTest {
public static void main(String[] args) {
try {
Socket socket =new Socket("127.0.0.1",9999);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream =new DataOutputStream(outputStream);
            Scanner scanner =new Scanner(System.in);
            if(scanner.hasNext()){
            String str = scanner.next();
                int type =1;
                byte[] data = str.getBytes();
                int len = data.length +5;
                dataOutputStream.writeByte(type);
                dataOutputStream.writeInt(len);
                dataOutputStream.write(data);
                dataOutputStream.flush();
            }
}catch (IOException e) {
e.printStackTrace();
        }
}
}

