package com.mailserver;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mailserver.maildb.MaildbOP;

public class SocketServerMailTest {
	
	public static int port=25;

	public static void main(String[] args) throws SQLException {
	//	MaildbOP maildb;
		try {
			// 数据库
			 //maildb =new MaildbOP();
			 
			
			// 创建服务端socket
			ServerSocket serverSocket = new ServerSocket( port );
			// 创建客户端socket
			Socket socket = new Socket();
			// 循环监听等待客户端的连接
			while (true) {
				// 监听客户端
				socket = serverSocket.accept();
				// ServerThreadMailTest thread = new ServerThreadMailTest(socket,maildb);
				ServerThreadMailTest thread = new ServerThreadMailTest(socket);
				
				thread.start();
				InetAddress address = socket.getInetAddress();
				System.out.println("当前客户端的IP：" + address.getHostAddress());
				
				Date date = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss E");
				String format = simpleDateFormat.format(date); // format:将日期转换成制定格式的字符串
				System.out.println("当前时间=" + format);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			 
			e.printStackTrace();
		} finally {
			 
		}
	}

}