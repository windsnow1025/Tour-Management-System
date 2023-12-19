package com.windsnow1025.tourmanagementsystem.swing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class MySQLDynamicQueryPage extends JFrame {
    private JTextField[] inputFields;
    private JButton addButton, deleteButton, updateButton, searchButton,returnButton;
    private JTextArea resultArea;
    String tableName;

    ArrayList<String> columnNames = new ArrayList<>();

    public MySQLDynamicQueryPage(String selectedTable) {
        setTitle("MySQL Dynamic Query Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        tableName = selectedTable;


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
        addButton.addActionListener(e -> addRecord());
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteRecord());
        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateRecord());
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRecord());

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(updateButton);
        inputPanel.add(searchButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        // 创建返回按钮
        returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            TableSelectionPage tableSelectionPage = new TableSelectionPage();
            tableSelectionPage.setVisible(true);
            dispose(); // 关闭当前页面
        });
        inputPanel.add(returnButton); // 将返回按钮添加到界面中
    }

    private void addRecord() {
        try (Connection connection = MyConnection.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");
            for (int i = 0; i < columnNames.size(); i++) {
                queryBuilder.append("`").append(columnNames.get(i)).append("`");
                if (i < columnNames.size() - 1) {
                    queryBuilder.append(", ");
                } else {
                    queryBuilder.append(") VALUES (");
                }
            }
            for (int i = 0; i < inputFields.length; i++) {
                queryBuilder.append("?");
                if (i < inputFields.length - 1) {
                    queryBuilder.append(", ");
                } else {
                    queryBuilder.append(")");
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                for (int i = 0; i < inputFields.length; i++) {
                    statement.setString(i + 1, inputFields[i].getText());
                }
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    resultArea.setText("A new record was inserted successfully!");
                }
            }
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
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