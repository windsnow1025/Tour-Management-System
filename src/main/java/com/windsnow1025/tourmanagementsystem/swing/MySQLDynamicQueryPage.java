package com.windsnow1025.tourmanagementsystem.swing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class MySQLDynamicQueryPage extends JFrame {
    private JTextField[] inputFields;
    private JButton addButton, deleteButton, updateButton, searchButton,returnButton,viewTableButton;
    private JTextArea resultArea;
    String tableName;

    ArrayList<String> columnNames = new ArrayList<>();

    public MySQLDynamicQueryPage(String selectedTable) {
        setTitle("MySQL Dynamic Query Page");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableName = selectedTable;
        ArrayList<String> columnNames = new ArrayList<>();

        try (Connection connection = MyConnection.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
            int columnCount = 0;
            while (resultSet.next()) {
                columnCount++;
            }
            inputFields = new JTextField[columnCount];

            resultSet = metaData.getColumns(null, null, tableName, null);
            int index = 0;
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                columnNames.add(columnName);
                inputPanel.add(new JLabel(columnName + ":"));
                inputFields[index] = new JTextField();
                inputPanel.add(inputFields[index]);
                index++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        searchButton = new JButton("Search");
        returnButton = new JButton("Return");

        addButton.addActionListener(e -> addRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        updateButton.addActionListener(e -> updateRecord());
        searchButton.addActionListener(e -> searchRecord());
        returnButton.addActionListener(e -> {
            TableSelectionPage tableSelectionPage = new TableSelectionPage();
            tableSelectionPage.setVisible(true);
            dispose(); // 关闭当前页面
        });

        // 创建查看表按钮
        viewTableButton = new JButton("View");
        viewTableButton.addActionListener(e -> {
            ViewDisplayPage tableDisplayPage = new ViewDisplayPage(selectedTable);
            tableDisplayPage.setVisible(true);
        });
        inputPanel.add(viewTableButton); // 将查看表按钮添加到界面中

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(viewTableButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);

        resultArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        add(resultArea, BorderLayout.SOUTH);
        //add(returnButton, BorderLayout.SOUTH);

    }

    private void addRecord() {
        try (Connection connection = MyConnection.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");

            // Append column names
            for (int i = 0; i < columnNames.size(); i++) {
                queryBuilder.append("`").append(columnNames.get(i)).append("`");
                if (i < columnNames.size() - 1) {
                    queryBuilder.append(", ");
                }
            }

            queryBuilder.append(") VALUES (");

            // Append placeholders for values
            for (int i = 0; i < inputFields.length; i++) {
                queryBuilder.append("?");
                if (i < inputFields.length - 1) {
                    queryBuilder.append(", ");
                }
            }

            queryBuilder.append(")");

            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                // Set values for placeholders
                for (int i = 0; i < inputFields.length; i++) {
                    statement.setString(i + 1, inputFields[i].getText());
                }

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    resultArea.setText("成功插入新记录！");
                }
            }
        } catch (SQLException ex) {
            resultArea.setText("错误：" + ex.getMessage());
        }
    }



    private void deleteRecord() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID to delete:"));
        try (Connection connection = MyConnection.getConnection()) {
            String query = "DELETE FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    resultArea.setText("The record was deleted successfully!");
                } else {
                    resultArea.setText("Record not found!");
                }
            }
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void updateRecord() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID to update:"));
        String columnName = JOptionPane.showInputDialog("Enter column name to update:");
        String newValue = JOptionPane.showInputDialog("Enter new value:");
        try (Connection connection = MyConnection.getConnection()) {
            String query = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, newValue);
                statement.setInt(2, id);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    resultArea.setText("The record was updated successfully!");
                } else {
                    resultArea.setText("Record not found!");
                }
            }
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void searchRecord() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID to search:"));
        try (Connection connection = MyConnection.getConnection()) {
            String query = "SELECT * FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        StringBuilder result = new StringBuilder();
                        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                            result.append(resultSet.getMetaData().getColumnName(i)).append(": ").append(resultSet.getString(i)).append("\n");
                        }
                        resultArea.setText(result.toString());
                    } else {
                        resultArea.setText("Record not found!");
                    }
                }
            }
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }


}