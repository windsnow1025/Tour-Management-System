package com.windsnow1025.tourmanagementsystem.swing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static final String HOST_ADDRESS="localhost";
    private static final String DATABASE_NAME="tour";
    private static final String USER="root";
    private static final String PASSWORD="Fx021219";
    private static final String DBURL="jdbc:mysql://"+HOST_ADDRESS+":3306/"+DATABASE_NAME+"?useSSL=false&serverTimezone=UTC";
    public static Connection getConnection(){
        Connection connection =null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=java.sql.DriverManager.getConnection(DBURL,USER,PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}
