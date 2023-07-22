package com.mailserver.maildb;

 
 

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


 

 
public class JdbcTest {
    /**
     * 连接地址
     */
    public static final String URL = "jdbc:mysql://127.0.0.1:3306/maildb";
    /**
     * 用户名
     */
    public static final String USER = "root";
    
    /**
     * 密码
     */
    public static final String PASSWORD = "111111$$$";

    /**
     * 连接
     */
    private static Connection conn = null;

    static {
        try {
            //1.加载驱动程序
           // Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 
            //2. 获得数据库连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 维护常量连接，不用每次都创建
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * 测试查询
     *
     * @throws SQLException
     */
    
    public void testQuery() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        

        stmt.execute("insert into mail(first_name) values('peterxy')");
        
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
        	System.out.println( rs.getString("first_name") );
        } 
    }
}
