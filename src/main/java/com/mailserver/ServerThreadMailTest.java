package com.mailserver;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.mailserver.maildb.MaildbOP;

public class ServerThreadMailTest extends Thread {

	private Socket socket = null;
	private int dataing = 0;
	private int MAILing = 0;
	private boolean testDebug = false;
	private String filename_from = "";
	private String filename_rcpt = "";
	private String filename_data = "";
	private String filename_debug = "";
	private String filetemp="";
	private String cmdFlag = "";
	private MaildbOP maildb;

	// public ServerThreadMailTest(Socket socket ,MaildbOP db) {

	public ServerThreadMailTest(Socket socket ) {
		this.socket = socket; 
	    this.maildb = new MaildbOP();
	}

	public String parseCmd(String cmdstr) {
		String cmd = "";
		// System.out.println("cmdstrlength=" + cmdstr.length());
		if (cmdstr.length() > 0) {
			// String dot = cmdstr.substring(cmdstr.length()-1, cmdstr.length());
			if (cmdstr.equals(".")) {
				cmd = ".";
			} else if (cmdstr.length() >= 4) {
				cmd = cmdstr.substring(0, 4);
			}
		}
		cmd = cmd.toUpperCase();
		// System.out.println("cmd=" + cmd);
		if (this.cmdFlag.equals("AUTH")) {
			cmd = "LOGIN";
		}
		if (this.cmdFlag.equals("LOGIN")) {
			cmd = "PASS";
		}
		return cmd;
	}

//	panhongweideMacBook-Pro:~ panhw$ telnet 163mx01.mxmail.netease.com  25
//	Trying 220.181.12.117...
//	Connected to 163mx01.mxmail.netease.com.
//	Escape character is '^]'.
//	220 163.com Anti-spam GT for Coremail System (163com[20141201])
//	HELO 163.com 
//	250 OK
//	mail from:<peter@mailserver.plus>
//	250 Mail OK
//	rcpt to:<phw95@163.com>
//	250 Mail OK
//	data
//	354 End data with <CR><LF>.<CR><LF>
//	from:<peter@mailserver.plus>
//	to:<phw95@163.com>
//	subject:hi
//
//	cccccc
//	.
//	451 DT:SPM 163 zwqz-mx-mta-g3-1,_____wCXz9f+_S9ktHAQAQ--.9992S2 1680866912, please try again 15min later
//	quit
//	221 Bye
//	Connection closed by foreign host.

	public void sendData(PrintWriter pw, String returnstring) {
		pw.write(returnstring);
		pw.write("\r\n");
		pw.flush();
	}

	public String getFileName() {
		Random r = new Random();
		int i = r.nextInt(10000);

		// 默认创建
//		Date date0 = new Date();
//		long t = date0.getTime();
		
		long t=System.currentTimeMillis()/1000L; 
		String filename = String.valueOf(t) + "_" + String.valueOf(i);
		this.filetemp = filename;
		System.out.println("filename:" + filename);
		return filename;

	}

	public void doFileWriter(String fileName, String content) throws IOException {
		FileWriter writer = new FileWriter(fileName, true);
		writer.write(content);
		writer.close();
	}

	@Override
	public void run() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		PrintWriter pw = null;
		FileWriter writer = null;
		String info = null;
		String returnstring = "";
		String filetemp = this.getFileName();  
		filetemp = "/Users/panhw/jmail/" + filetemp;
		
		
		
		this.filename_from = filetemp.concat("_from.txt");
		this.filename_rcpt = filetemp.concat("_rcpt.txt");
		this.filename_data = filetemp.concat("_data.txt");
		this.filename_debug = "/Users/panhw/jmail/debug.txt";
		this.testDebug = true;

		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(this.filename_data, true);
			os = socket.getOutputStream();
			pw = new PrintWriter(os);
			pw.write("220 mailserver.plus server");
			pw.write("\r\n");
			pw.flush();

			while ((info = br.readLine()) != null) {
				// System.out.println("我是服务器，客户端说：" + info);
				if (this.testDebug == true) {
					this.doFileWriter(this.filename_debug, info + "\r\n");
				}

				String flag = this.parseCmd(info);
				// System.out.println("flagx=" + flag);

				if (this.dataing == 1 && !info.equals(".")) {
					try {

						writer.write(info);
						writer.write("\r\n");

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				switch (flag) {
				case "EHLO":
					returnstring = "250-mail\r\n250-PIPELINING\r\n250-AUTH LOGIN PLAIN\r\n250-AUTH=LOGIN PLAIN\r\n250 8BITMIME";
					this.sendData(pw, returnstring);
					break;
				case "HELO":
					returnstring = "250 OK";
					this.sendData(pw, returnstring);
					break;
				case "MAIL": // 250 Mail OK
					if (this.MAILing !=1 ) { 
						this.MAILing=1;
						returnstring = "250 Mail OK";
						this.sendData(pw, returnstring);
						this.doFileWriter(this.filename_from, info);
					}
					break;
				case "RCPT": // 250 Mail OK
					returnstring = "250 Mail OK";
					this.sendData(pw, returnstring);
					this.doFileWriter(this.filename_rcpt, info);
					//判断是否有peter
					if ( info !=null ) {
						if ( info.indexOf("peter")>0 ) {
							CallPhone cp =new CallPhone();
							cp.httpURLGETCase( "18917133237");
						}else if (  info.indexOf("lyh")>0  ) {
							CallPhone cp =new CallPhone();
							cp.httpURLGETCase( "13358197773");
						}
					}
					break;
				case "DATA": // 354 End data with <CR><LF>.<CR><LF>
					if ( this.dataing!=1 ) { 
						returnstring = "354 End data with <CR><LF>.<CR><LF>";
						this.dataing = 1;
						this.sendData(pw, returnstring); 
						System.out.println("for DATA returnstring:"+ returnstring);
					}
					break;
				case ".": // 250 OK
					returnstring = "250 OK";
					this.dataing = 0;
					this.sendData(pw, returnstring);
					System.out.println("for . returnstring:"+ returnstring);
					// 关闭资源
					try {
						// 关闭文件句柄
						if (writer != null)
							writer.close();
					}catch (IOException e) {
						e.printStackTrace();
					}
					//insert db 
					
					//解析data文件，写如数据表： tbl_address ,tbl_attachment
					
					this.maildb.opendb();
					this.maildb.doinsertDB(this.filetemp);
					this.maildb.closedb();
					
					break;
				case "QUIT": // 221 Bye
					returnstring = "221 Bye";
					this.sendData(pw, returnstring);
//					if (socket != null)
//						socket.close();
					break;
				case "AUTH": // tell server start login, and tell client enter username
					if ( info.split(" ").length ==2 ) {
						returnstring = "334 dXNlcm5hbWU6";
						this.sendData(pw, returnstring);
					}else { // AUTH LOGIN XXXXX
						flag = "LOGIN";
						returnstring = "334 UGFzc3dvcmQ6";
						this.sendData(pw, returnstring);
					}
					break;
				case "LOGIN": // tell server username , and tell client enter password
					returnstring = "334 UGFzc3dvcmQ6";
					this.sendData(pw, returnstring);
					break;
				case "PASS": // tell server password , and tell client yes or no
					returnstring = "235 2.7.0 Authentication successful";
					this.sendData(pw, returnstring);
					break;

				}
				this.cmdFlag = flag;
				if ( this.cmdFlag.equals("QUIT") ) {
					System.out.println("this.cmdFlag:"+this.cmdFlag);
//					if (socket != null) {
//						socket.close();
//					    System.out.println("close socket");
//					}
					break;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error:");
			System.out.println(e.getMessage());
		} finally {
			// 关闭资源
			try {
 

				if (pw != null)
					pw.close();
				if (os != null)
					os.close();
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (is != null)
					is.close();
				if (socket != null) {
					socket.close();
				    System.out.println("do close socket in finally");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}