package com.mailserver.r;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import com.mailserver.CallPhone;
import com.mailserver.file.MSFile;
import com.mailserver.maildb.JdbcTest;
import com.mailserver.maildb.MaildbOP;

public class testDo {

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub

		Random r = new Random();
		int i = r.nextInt(10000);

		// 默认创建
		Date date0 = new Date();
		String filename = String.valueOf(date0.getTime()) + "_" + String.valueOf(i);
		System.out.println("filename:" + filename);
		System.out.println("time:" + date0);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss E");
		String format = simpleDateFormat.format(date0); // format:将日期转换成制定格式的字符串
		System.out.println("当前时间=" + format);

//		JdbcTest jt =new JdbcTest();
//		jt.testQuery();

		long rest = System.currentTimeMillis() / 1000L;
		System.out.println("rest:" + String.valueOf(rest));

//		MSFile f = new MSFile();
//		try {
//			String c = f.fileGetContents( "/Users/panhw/jmail/1681816351_7681_data.txt" );
//		//	System.out.println(c);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// 1681816351_7681_data
 
//		MaildbOP db = new MaildbOP();
////		String x = db.parseSubject("1682082862_9193");
////		System.out.println(x);
//		db.testQuery();
		
//		
//		File file_fid_folder = new File( "/Users/panhw/jmail/108/");
//		boolean newjavaFile = file_fid_folder.mkdir();
		
		
		CallPhone cp =new CallPhone();
		cp.httpURLGETCase( "18917133237");

	}

}
