package com.mailserver.maildb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

 

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.mail.internet.MimeUtility;

import com.mailserver.SocketServerMailTest;
import com.mailserver.eml;
import com.mailserver.file.MSFile;

public class MaildbOP {
	/**
	 * 连接地址
	 */
	public static final String URL = "jdbc:mysql://127.0.0.1:3306/maildb?autoReconnect=true&autoReconnectForPools=true";
	/**
	 * 用户名
	 */
	public static final String USER = "maildb";

	/**
	 * 密码
	 */
	public static final String PASSWORD = "pass";
	// public static final String PASSWORD = "111111";

	/**
	 * 连接
	 */
	private static Connection conn = null;

	private static Statement stmt = null;

	private static String sid;

//	static {
//		try {
//			// 1.加载驱动程序
//			// Class.forName("com.mysql.jdbc.Driver");
//			Class.forName("com.mysql.cj.jdbc.Driver");
//
//			//
//			// 2. 获得数据库连接
//			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//		} catch (SQLException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 维护常量连接，不用每次都创建
	 */
//	public static Connection getConnection() {
//		return conn;
//	}

	public void closedb() throws SQLException {
		this.stmt.close();
		this.conn.close();
	}

	/**
	 * 测试查询
	 *
	 * @throws SQLException
	 * @throws IOException
	 */

	public void opendb() {
		try {
			// 1.加载驱动程序
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");

			//
			// 2. 获得数据库连接
			this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String parseSubject(String filename) throws IOException {

		String fileNamex = "/Users/panhw/jmail/" + filename + "_data.txt";
		StringBuffer sb1 = new StringBuffer("");

		String subject = "";
		String subject2 = "";
		int subjectFlag = 0;

		try (Scanner sc = new Scanner(new FileReader(fileNamex))) {
			while (sc.hasNextLine()) { // 按行读取字符串
				String line = sc.nextLine();
				// subject 判断是否已经读过
				if (subjectFlag == 1) {
					if (line.trim().indexOf("=?") == 0) {
						subject2 = line;
						sb1.append("\n");
						sb1.append(subject2);
					} else {
						break;
					}
				}
				// subject 判断是否有
				if (line.toLowerCase().indexOf("subject") == 0) {
					subject = line;
					subject = subject.substring(8, subject.length());
					// break;
					subjectFlag = 1;
					sb1.append(subject);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(sb1.toString());

		return sb1.toString();

	}

	public void doinsertDB(String filename) throws SQLException, IOException {

		// filename:1682998483_4320

		this.stmt = conn.createStatement();

		long t = System.currentTimeMillis() / 1000L;
		MSFile msfile = new MSFile();
		String mfilepath = "/Users/panhw/jmail/" + filename + "_from.txt";
		String mfrom = msfile.fileGetContents(mfilepath);

		String tofilepath = "/Users/panhw/jmail/" + filename + "_rcpt.txt";
		String mto = msfile.fileGetContents(tofilepath);

		String datafilepath = "/Users/panhw/jmail/" + filename + "_data.txt";
		// String mdata = msfile.fileGetContents(datafilepath);

		String subject = this.parseSubject(filename);
		String type = "";

		// if (mfrom.toUpperCase().indexOf("MAILSERVER.PLUS") >= 0) {
		if (SocketServerMailTest.port == 26) {

			type = "smtp";
			
//			stmt.execute("insert into tbl_smtp(mfrom,mto,subject,content,status,created) values('" + mfrom + "','" + mto
//					+ "','" + subject + "','" + mdata + "',0," + t + ")");
			
			
			
			String sql =  "insert into tbl_smtp(mfrom,mto,subject,status,filename,created) values(?,?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, mfrom);
			ps.setString(2, mto);
			ps.setString(3, subject); 
			ps.setInt(4, 0); 
			ps.setString(5, filename);
			ps.setLong(6, t);
			ps.executeUpdate();
			ResultSet rsx = ps.getGeneratedKeys();
			while (rsx.next()) {
				int id = rsx.getInt(1);// 回指定列的值
				System.out.println("自增id=" + id);
			}
			
			
		} else {

			type = "inbox";
//			String sql = "insert into tbl_inbox(mfrom,mto,subject,content,created)       values('" + mfrom + "','" + mto
//					+ "','" + subject + "','" + mdata + "'," + t + ")";
//			stmt.execute(sql);
			
			String sql = "insert into tbl_inbox(mfrom,mto,subject,filename,created)       values(?,?,?,?,?)";
			
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			System.out.println("mfrom:"+ mfrom);
			ps.setString(1, mfrom);
			ps.setString(2, mto);
			ps.setString(3, subject); 
			ps.setString(4, filename);
			ps.setLong(5, t);
			ps.executeUpdate();
			ResultSet rsx = ps.getGeneratedKeys();
			while (rsx.next()) {
				int id = rsx.getInt(1);// 回指定列的值
				System.out.println("自增id=" + id);
			}

		}

		ResultSet rslid = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
		while (rslid.next()) {
			// System.out.println(rslid.getString("id"));
			sid = rslid.getString("id");
		}

		System.out.println("写入数据库成功.");
		System.out.println("id=" + sid);
		System.out.println("do eml e = new eml()");
 
		
		eml ex = new eml();
		ex.myList = new ArrayList();
		ex.myListTos = new ArrayList();
		ex.pmail(datafilepath, sid);

//		email_content_type = msg.getContentType().toString();
//		email_content = msg.getContent().toString();

	 	// if (type.equals("inbox")) {
		if (SocketServerMailTest.port == 26) {	
			

//			String sql = "update  tbl_smtp set subject = '" + ex.subject + "',  bodytype ='" + ex.email_content_type
//					+ "', body='" + ex.email_content + "' where sid=" + sid;
//			stmt.execute(sql);
			
			
			
			String sql =  "update  tbl_smtp set subject = ?,  bodytype =?, body=?  where sid=?";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, ex.subject);
			ps.setString(2, ex.email_content_type);
			ps.setString(3, ex.email_content);
			ps.setInt(4,  Integer.parseInt(sid));
			ps.executeUpdate();
			ResultSet rsx = ps.getGeneratedKeys();
			while (rsx.next()) {
				int id = rsx.getInt(1);// 回指定列的值
				System.out.println("自增 for update id=" + id);
			}
			
			
		} else {
//			String sql = "update  tbl_inbox set subject = '" + ex.subject + "',  bodytype ='" + ex.email_content_type
//					+ "', body='" + ex.email_content + "' where sid=" + sid;  
//			stmt.execute(sql);
			
			String sql =   "update  tbl_inbox set subject = ?,  bodytype =?, body=? where sid=?";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, ex.subject);
			ps.setString(2, ex.email_content_type);
			ps.setString(3, ex.email_content);
			ps.setInt(4,  Integer.parseInt(sid));
			ps.executeUpdate();
			ResultSet rsx = ps.getGeneratedKeys();
			while (rsx.next()) {
				int id = rsx.getInt(1);// 回指定列的值
				System.out.println("自增 for update id=" + id);
			}
			
			
		}

		System.out.println(ex.myList);
		System.out.println(ex.myListTos);
		System.out.println(ex.email_content_type);
		System.out.println(ex.email_content);

//		mysql> show fields from tbl_address;
//		+---------+-------------+------+-----+---------+----------------+
//		| Field   | Type        | Null | Key | Default | Extra          |
//		+---------+-------------+------+-----+---------+----------------+
//		| aid     | int         | NO   | PRI | NULL    | auto_increment |
//		| sid     | int         | NO   |     | NULL    |                |
//		| type    | varchar(10) | YES  |     | NULL    |                |
//		| name    | varchar(50) | YES  |     | NULL    |                |
//		| addr    | varchar(50) | YES  |     | NULL    |                |
//		| created | int         | YES  |     | NULL    |                |
//		+---------+-------------+------+-----+---------+----------------+

		for (int x = 0; x < ex.myListTos.size(); x++) {
			List list = (List) ex.myListTos.get(x);
			String name = (String) list.get(0);
			String addr = (String) list.get(1);
			String sql = "insert into tbl_address(sid,type,name,addr,created)       values('" + sid + "','" + type
					+ "','" + name + "','" + addr + "'," + t + ")";
			stmt.execute(sql);
		}

		System.out.println("eml.myList.size:" + ex.myList.size());

		for (int x = 0; x < ex.myList.size(); x++) {

//			index:0 myList_item.add(  strFileNmae ); 
//		    index:1 myList_item.add(MimeUtility.decodeText(part.getContentType()));
//		    index:2 myList_item.add( part.getDisposition() );
//		    index:3 myList_item.add( mp.getContentID() );

//			mysql> show fields from tbl_attachment;
//			+----------+--------------+------+-----+---------+----------------+
//			| Field    | Type         | Null | Key | Default | Extra          |
//			+----------+--------------+------+-----+---------+----------------+
//			| aid      | int          | NO   | PRI | NULL    | auto_increment |
//			| sid      | int          | NO   |     | NULL    |                |
//			| type     | varchar(10)  | YES  |     | NULL    |                |
//			| filename | varchar(50)  | YES  |     | NULL    |                |
//			| filepath | varchar(50)  | YES  |     | NULL    |                |
//			| cid      | varchar(200) | YES  |     | NULL    |                |
//			| created  | int          | NO   |     | NULL    |                |
//			+----------+--------------+------+-----+---------+----------------+
//			7 rows in set (0.00 sec)

			List list = (List) ex.myList.get(x);

			String fname = (String) list.get(0);
			String ctype = (String) list.get(1);
			String dis = (String) list.get(2);
			String cid = (String) list.get(3);
			String filepath = "";

			String sql = "insert into tbl_attachment(sid,type,filename,filepath,cid,created)       values('" + sid
					+ "','" + type + "','" + fname + "','" + filepath + "','" + cid + "'," + t + ")";
			stmt.execute(sql);
		}

		// tbl_inbox
//         $data = array(
//        	        "mfrom" => $fname_from_c,
//        	        "mto" => $fname_rcpt_c,
//        	        "subject" => $subject,
//        	        "content" => $content,
//        	        "created" => time()
//        	    );

	}

	public void testQuery() throws SQLException {

		this.opendb();
		Statement stmt = conn.createStatement();

		// stmt.execute("insert into mail(first_name) values('peterxy')");

		String sql = "insert into mail(first_name) values(?)"; 
		PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, "tpl");
		ps.executeUpdate();
		ResultSet rsx = ps.getGeneratedKeys();
		while (rsx.next()) {
			int id = rsx.getInt(1);// 回指定列的值
			System.out.println("自增id=" + id);
		}

		ResultSet rs = stmt.executeQuery("SELECT * FROM mail limit 10");

		while (rs.next()) {
//            author = new Authors();
//            author.setId(rs.getInt("id"));
//            author.setFirstName(rs.getString("first_name"));
//            author.setLastName(rs.getString("last_name"));
//            author.setEmail(rs.getString("email"));
//            author.setBirthdate(rs.getDate("birthdate"));
//            author.setAdded(rs.getDate("added"));
//            System.out.println(author);
//            authorsList.add(author);
			System.out.println(rs.getString("first_name"));
		}

		ResultSet rslid = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");

		while (rslid.next()) {
//            author = new Authors();
//            author.setId(rs.getInt("id"));
//            author.setFirstName(rs.getString("first_name"));
//            author.setLastName(rs.getString("last_name"));
//            author.setEmail(rs.getString("email"));
//            author.setBirthdate(rs.getDate("birthdate"));
//            author.setAdded(rs.getDate("added"));
//            System.out.println(author);
//            authorsList.add(author);
			System.out.println(rslid.getString("id"));
		}
	}
}
