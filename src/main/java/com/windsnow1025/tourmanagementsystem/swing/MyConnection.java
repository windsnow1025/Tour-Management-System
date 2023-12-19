package com.windsnow1025.tourmanagementsystem.swing;

import com.windsnow1025.tourmanagementsystem.db.JDBCHelper;

import java.sql.Connection;
import java.sql.SQLException;

public class MyConnection {
    public static Connection getConnection() throws SQLException {
        JDBCHelper jdbcHelper = new JDBCHelper();
        return jdbcHelper.getConnection();
    }

}
