package com.windsnow1025.tourmanagementsystem.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class TableSelectionPage extends JFrame {
    private JComboBox<String> tableComboBox;
    private JComboBox<String> viewComboBox;
    private JButton selectButton;
    private JButton viewButton;
    private String schema_name = "tour";
    private String catalog = "tour";


    public TableSelectionPage() {
        setTitle("Table Selection Page");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 获取数据库中的表名
        ArrayList<String> tableNames = new ArrayList<>();
        try (Connection connection = MyConnection.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(catalog, schema_name, null, new String[] {"TABLE"});
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // 获取数据库中用户创建的视图名
        ArrayList<String> viewNames = new ArrayList<>();
        try (Connection connection = MyConnection.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet views = metaData.getTables(catalog, schema_name, null, new String[] {"VIEW"}); // 将 "your_schema_name" 替换为您的模式名称
            while (views.next()) {
                viewNames.add(views.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tableComboBox = new JComboBox<>(tableNames.toArray(new String[0]));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(tableComboBox, gbc);

        selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            String selectedTable = (String) tableComboBox.getSelectedItem();
            MySQLDynamicQueryPage dynamicQueryPage = new MySQLDynamicQueryPage(selectedTable);
            dynamicQueryPage.setVisible(true);
            dispose();
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(selectButton, gbc);

        viewComboBox = new JComboBox<>(viewNames.toArray(new String[0]));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(viewComboBox, gbc);

        viewButton = new JButton("View");
        viewButton.addActionListener(e -> {
            String selectedView = (String) viewComboBox.getSelectedItem();
            ViewDisplayPage viewDisplayPage = new ViewDisplayPage(selectedView);
            viewDisplayPage.setVisible(true);
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(viewButton, gbc);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TableSelectionPage app = new TableSelectionPage();
            app.setVisible(true);
        });
    }
}