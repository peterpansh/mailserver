package com.mailserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;

public class eml {

	// java -cp
	// /Users/panhw/.m2/repository/javax/mail/mail/1.5.0-b01/mail-1.5.0-b01.jar:/Users/panhw/.m2/repository/javax/mail/mail/1.5.0-b01/mail-1.5.0-b01.jar:/Users/panhw/.m2/repository/javax/activation/activation/1.1/activation-1.1.jar:msparsemail.jar
	// com.mailserver.eml

//	public static String email_content_type;
//	public static String email_content;
//	public static List myList = new ArrayList();
//	public static List myListTos = new ArrayList();
//	public static String fid;
//	public static String root_folderpath = "/Users/panhw/jmail/";
//	public static int atti=0;
//	public static String subject;
	
	// 改为非静态变量
	public   String email_content_type;
	public   String email_content;
	public   List myList = new ArrayList();
	public   List myListTos = new ArrayList();
	public   String fid;
	public   String root_folderpath = "/Users/panhw/jmail/";
	public   int atti=0;
	public   String subject;
	

	public static void main(String args[]) throws Exception {

		eml ex=new eml();
		
		String fn = "/Users/panhw/Downloads/cmb.eml";
		fn = "/Users/panhw/Documents/jpg.eml";
		fn = "/Users/panhw/Downloads/fjtwbm.eml";
		// fn="/Users/panhw/Downloads/test.eml";
		if (args.length > 1) {
			System.out.println(args[1]);
		}

		Files.walkFileTree(Paths.get(fn), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				try {
					if (file.toFile().getAbsolutePath().endsWith(".eml")) {
						ex.parserFile(file.toFile().getAbsolutePath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return super.visitFile(file, attrs);
			}
		});

	}

	public void pmail(String filename, String fidtemp) throws IOException {

		String fn = "/Users/panhw/Downloads/cmb.eml";
		fn = "/Users/panhw/Documents/jpg.eml";
		fn = "/Users/panhw/Downloads/fjtwbm.eml";
		// fn="/Users/panhw/Downloads/test.eml";
		
		atti=0;

		File file_fid_folder = new File( root_folderpath  + fidtemp);
		
		System.out.println("file_fid_folder:");
		System.out.println(file_fid_folder);
		
		boolean newjavaFile = file_fid_folder.mkdir();
		System.out.println("newjavaFile:");
		System.out.println(newjavaFile);

	

		fn = filename;
		fid= fidtemp;

		Files.walkFileTree(Paths.get(fn), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				try {
					if (file.toFile().getAbsolutePath().endsWith(".eml")
							|| file.toFile().getAbsolutePath().endsWith(".txt")) {
						parserFile(file.toFile().getAbsolutePath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return super.visitFile(file, attrs);
			}
		});

	}

	// http://blog.csdn.net/aassdd_zz/article/details/8204344
	public  void parserFile(String emlPath) throws Exception {
		System.out.println(emlPath);
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		InputStream inMsg;
		inMsg = new FileInputStream(emlPath);
		Message msg = new MimeMessage(session, inMsg);
		parseEml(msg);
	}

	private  void parseEml(Message msg) throws Exception {
		// 发件人信息
		Address[] froms = msg.getFrom();
		Address[] tos = msg.getAllRecipients();
		if (froms != null) {
			// System.out.println("发件人信息:" + froms[0]);
			InternetAddress addr = (InternetAddress) froms[0];
			System.out.println("发件人地址:" + addr.getAddress());
			System.out.println("发件人显示名:" + addr.getPersonal());
			System.out.println("\n");

			for (int i = 0; i < tos.length; i++) {

				InternetAddress addr_to_2 = (InternetAddress) tos[i];
				System.out.println("收件人地址:" + addr_to_2.getAddress());
				System.out.println("发收件人显示名:" + addr_to_2.getPersonal());
				System.out.println("\n");

				List myList_item = new ArrayList();
				myList_item.add(addr_to_2.getPersonal());
				myList_item.add(addr_to_2.getAddress());
				myListTos.add(myList_item);

			}

		}
		System.out.println("邮件主题:" + msg.getSubject());
		subject =  msg.getSubject();

		//
		System.out.println("----解析start-----");
		// getContent() 是获取包裹内容, Part相当于外包装
		Object o = msg.getContent();
		if (o instanceof Multipart) { // 多个
			Multipart multipart = (Multipart) o;
			reMultipart(multipart);
		} else if (o instanceof Part) { // 单个
			Part part = (Part) o;
			rePart(part);
		} else {
			// 纯内容
			System.out.println("-------纯内容-------");
			// System.out.println("类型" + msg.getContentType());
			// System.out.println("内容" + msg.getContent());
			email_content_type = msg.getContentType().toString();
			email_content = msg.getContent().toString();
		}
		System.out.println("----解析end-----");

		System.out.println("类型:" + email_content_type);
		System.out.println("内容:\n" + email_content);
		System.out.println("myList:");
		System.out.println(myList);

		System.out.println("myListTos:");
		System.out.println(myListTos);

	}

	/**
	 * @param part 解析内容
	 * @throws Exception
	 */
	private  void rePart(Part part) throws Exception {

//		

		List myList_item = new ArrayList();
		if (part.getDisposition() != null) { // 有附件
			String strFileNmae = part.getFileName();
			if (!StringUtils.isEmpty(strFileNmae)) { // MimeUtility.decodeText解决附件名乱码问题
				strFileNmae = MimeUtility.decodeText(strFileNmae);
				myList_item.add(strFileNmae);
				
				String fpath  = this.root_folderpath + fid + "/" +  String.valueOf (atti) + "_" + strFileNmae;
				System.out.println("fpath:");
				System.out.println( fpath);
				System.out.println("发现附件: " + strFileNmae);
				System.out.println("strFileNmae:" + strFileNmae);
				InputStream in = part.getInputStream();// 打开附件的输入流
				// 读取附件字节并存储到文件中
				java.io.FileOutputStream out = new FileOutputStream(fpath);
				int data;
				while ((data = in.read()) != -1) {
					out.write(data);
				}
				in.close();
				out.close();
				atti++;
			}
			System.out.println("内容类型: " + MimeUtility.decodeText(part.getContentType()));
			System.out.println("附件内容:" + part.getContent());
			System.out.println("getDisposition:" + part.getDisposition());
			MimePart mp = (MimePart) part;
			System.out.println("getContentID:" + mp.getContentID());

			myList_item.add(MimeUtility.decodeText(part.getContentType()));
			if (part.getDisposition() != null) {
				myList_item.add(part.getDisposition());
			} else {
				myList_item.add("");
			}

			if (mp.getContentID() != null) {
				myList_item.add(mp.getContentID());
			} else {
				myList_item.add("");
			}

			myList.add(myList_item);

		} else {
			if (part.getContentType().startsWith("text/plain")) {
				// System.out.println("文本内容：" + part.getContent());
				email_content_type = "text/plain";
				email_content = part.getContent().toString();
			} else {
				// System.out.println("HTML内容：" + part.getContent());
				email_content_type = "html/plain";
				email_content = part.getContent().toString();
			}
		}
	}

	/**
	 * @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)）
	 * @throws Exception
	 */
	private  void reMultipart(Multipart multipart) throws Exception {
		System.out.println("邮件共有" + multipart.getCount() + "部分组成");
		// 依次处理各个部分
		for (int j = 0, n = multipart.getCount(); j < n; j++) {
			// System.out.println("处理第" + j + "部分");
			Part part = multipart.getBodyPart(j);// 解包, 取出 MultiPart的各个部分,
													// 每部分可能是邮件内容,
			// 也可能是另一个小包裹(MultipPart)
			// 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
			if (part.getContent() instanceof Multipart) {
				Multipart p = (Multipart) part.getContent();// 转成小包裹
				// 递归迭代
				reMultipart(p);
			} else {
				rePart(part);
			}
		}
	}

	public  void test(String emlPath) {
		try {

			System.out.println(emlPath);
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			InputStream inMsg;
			inMsg = new FileInputStream(emlPath);
			Message msg = new MimeMessage(session, inMsg);

			String[] date = msg.getHeader("Date");
			Address[] from = msg.getFrom();
			for (Address address : from) {
				InternetAddress internetAddress = (InternetAddress) address;
				System.out.println(internetAddress.getAddress());
				System.out.println(internetAddress.getPersonal());
			}
			System.out.println(msg.getSubject());

			Address[] to = msg.getReplyTo();

			Object o = msg.getContent();

			if (msg.isMimeType("multipart/*") || msg.isMimeType("MULTIPART/*")) {
				System.out.println("multipart");
				Multipart mp = (Multipart) o;

				int totalAttachments = mp.getCount();
				if (totalAttachments > 0) {
					for (int i = 0; i < totalAttachments; i++) {
						Part part = mp.getBodyPart(i);
						String s = getMailContent(part);
						String attachFileName = part.getFileName();
						String disposition = part.getDisposition();
						String contentType = part.getContentType();
						if ((attachFileName != null && attachFileName.endsWith(".ics"))
								|| contentType.indexOf("text/calendar") >= 0) {
							String[] dateHeader = msg.getHeader("date");
						}

						System.out.println(s);
						System.out.println(attachFileName);
						System.out.println(disposition);
						System.out.println(contentType);
						System.out.println("==============");
					}
					inMsg.close();
				}
			} else if (o instanceof Part) {
				Part part = (Part) o;
				rePart(part);
			} else {
				System.out.println("类型" + msg.getContentType());
				System.out.println("内容" + msg.getContent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  String getMailContent(Part part) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1) {
			conname = true;
		}
		StringBuilder bodytext = new StringBuilder();
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent());
		} else {
		}
		return bodytext.toString();
	}

}