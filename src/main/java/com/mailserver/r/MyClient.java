package com.mailserver.r;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1",8852);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        String string;
        while(true)
        {
            string=bReader.readLine();
            dataOutputStream.writeUTF(string);
            System.out.println(dataInputStream.readUTF());
            dataOutputStream.flush();
            if(string.equals("bye"))
                break;
        }
        socket.close();
    }
}