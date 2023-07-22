package com.mailserver.r;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClientMailTest {

	public static void main(String[] args) throws InterruptedException {
		try {
			// 和服务器创建连接
			//Socket socket = new Socket("163mx01.mxmail.netease.com", 25);
			 Socket socket = new Socket("mx2.qq.com", 25);
			

			// 要发送给服务器的信息
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);

			ArrayList<String> sites = new ArrayList<String>();
//			sites.add("helo 163.com\r\n");
//			sites.add("mail from:<peter@mailserver.plus>\r\n");
//			sites.add("rcpt to:<phw95@163.com>\r\n");
//			sites.add("data\r\n");
//			sites.add("from:<peter@mailserver.plus>\r\nto:<phw95@163.com>\r\nsubject:subject\r\n\r\ncontent\r\n.\r\n");
//			sites.add("quit\r\n");
			
			/*
			 * 
			 * panhongweideMacBook-Pro:~ panhw$ telnet 163mx01.mxmail.netease.com  25
Trying 220.181.12.117...
Connected to 163mx01.mxmail.netease.com.
Escape character is '^]'.
220 163.com Anti-spam GT for Coremail System (163com[20141201])
HELO 163.com 
250 OK
mail from:<peter@mailserver.plus>
250 Mail OK
rcpt to:<phw95@163.com>
250 Mail OK
data
354 End data with <CR><LF>.<CR><LF>
from:<peter@mailserver.plus>
to:<phw95@163.com>
subject:hi

cccccc
.
451 DT:SPM 163 zwqz-mx-mta-g3-1,_____wCXz9f+_S9ktHAQAQ--.9992S2 1680866912, please try again 15min later
quit
221 Bye
Connection closed by foreign host.

			 */

			sites.add("helo qq.com\r\n");
			sites.add("mail from:<peter@mailserver.plus>\r\n");
			sites.add("rcpt to:<czcty@qq.com>\r\n");
			sites.add("data\r\n");
			sites.add("from:<peter@mailserver.plus>\r\nto:<czcty@qq.com>\r\nsubject:subject123\r\n\r\ncontent\r\n.\r\n");
			sites.add("quit\r\n");

			InputStream is = null;
			BufferedReader br = null;
			String info = null;
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));

			info = null;
			while ((info = br.readLine()) != null) {
				System.out.println("我是客户端，服务器返回信息：" + info);
				break;
			}

			for (int i = 0; i < sites.size(); i++) {
				System.out.print(sites.get(i));
				pw.write(sites.get(i));
				pw.flush();
				info = null;
				while ((info = br.readLine()) != null) {
					System.out.println("我是客户端，服务器返回信息：" + info);
					break;
				}
			}
			
			while ((info = br.readLine()) != null) {
				System.out.println("我是客户端，服务器返回信息：" + info);
				break;
			}

			br.close();
			is.close();
			os.close();
			pw.close();
			socket.close();
			System.out.println("end");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}