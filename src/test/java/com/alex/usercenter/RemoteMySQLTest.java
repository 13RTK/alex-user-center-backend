package com.alex.usercenter;

import org.junit.Test;

import java.sql.*;

public class RemoteMySQLTest {
    @Test
    public void demo() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://htyg79erxfdt.us-east-1.psdb.cloud/alex_demo?sslMode=VERIFY_IDENTITY",
                "e6fdewam9v0i",
                "pscale_pw_ma2tUHe3GD9Ys0tiuZQzVHprLwjdS3aftK7BHGocmpU");

        Statement statement = conn.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from demo1");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getString("address"));
        }
    }
}
