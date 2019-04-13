package org.dl4j.jdbc;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.*;


public class Test {

	public static void main(String[] args) {
		System.out.println("MySQL JDBC Example.");
        java.sql.Connection conn = null;
        String url = "jdbc:mysql://202.45.128.135:20918/cloudImage?autoReconnect=true&useSSL=false";
        String driver = "com.mysql.cj.jdbc.Driver";
        String userName = "root";
        String password = "password";
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName(driver);
            conn=DriverManager.getConnection(url, userName, password);
            stmt=conn.createStatement();
            String sql = "select count(*) as count from image";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString("count"));
            }
            // 关闭资源
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore
            }
        }

	}

}
