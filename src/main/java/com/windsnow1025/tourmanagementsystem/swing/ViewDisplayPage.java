package com.windsnow1025.tourmanagementsystem.swing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewDisplayPage extends JFrame {
    private JTextArea resultArea;

    public ViewDisplayPage(String selectedView) {
        setTitle("View Display Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // 执行查询视图的操作
        try (Connection connection = MyConnection.getConnection()) {
            String query = "SELECT * FROM " + selectedView;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                StringBuilder result = new StringBuilder();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(metaData.getColumnName(i)).append(": ").append(resultSet.getString(i)).append("\n");
                    }
                    result.append("\n");
                }
                resultArea.setText(result.toString());
            }
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

}