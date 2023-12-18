package com.windsnow1025.tourmanagementsystem.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MySQLDynamicQueryPage extends JFrame {
    private JTextField[] inputFields;
    private JButton addButton, deleteButton, updateButton, searchButton;
    private JTextArea resultArea;

    public MySQLDynamicQueryPage() {
        setTitle("MySQL Dynamic Query Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));

        try (Connection connection = MyConnection.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, "旅游分公司", null);
            int columnCount = 0;
            while (resultSet.next()) {
                columnCount++;
            }
            inputFields = new JTextField[columnCount];

            resultSet = metaData.getColumns(null, null, "旅游分公司", null);
            int index = 0;
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
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
    }

    private void addRecord() {
        try (Connection connection = MyConnection.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO 旅游分公司 (");
            for (int i = 0; i < inputFields.length; i++) {
                queryBuilder.append("`").append(inputFields[i].getName()).append("`");
                if (i < inputFields.length - 1) {
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
            String query = "DELETE FROM 旅游分公司 WHERE id = ?";
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
            String query = "UPDATE 旅游分公司 SET " + columnName + " = ? WHERE id = ?";
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
            String query = "SELECT * FROM 旅游分公司 WHERE id = ?";
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MySQLDynamicQueryPage app = new MySQLDynamicQueryPage();
            app.setVisible(true);
        });
    }
}