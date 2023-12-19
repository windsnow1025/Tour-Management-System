package com.windsnow1025.tourmanagementsystem.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class TableSelectionPage extends JFrame {
    private JComboBox<String> tableComboBox;
    private JButton selectButton;

    public TableSelectionPage() {
        setTitle("Table Selection Page");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout());

        // 获取数据库中的表名
        ArrayList<String> tableNames = new ArrayList<>();
        try (Connection connection = MyConnection.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] {"TABLE"});
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // 创建下拉框，并添加表名作为选项
        tableComboBox = new JComboBox<>(tableNames.toArray(new String[0]));
        panel.add(tableComboBox);

        // 创建确定按钮
        selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            String selectedTable = (String) tableComboBox.getSelectedItem();
            MySQLDynamicQueryPage dynamicQueryPage = new MySQLDynamicQueryPage(selectedTable);
            dynamicQueryPage.setVisible(true);
            dispose(); // 关闭当前页面
        });
        panel.add(selectButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TableSelectionPage app = new TableSelectionPage();
            app.setVisible(true);
        });
    }
}