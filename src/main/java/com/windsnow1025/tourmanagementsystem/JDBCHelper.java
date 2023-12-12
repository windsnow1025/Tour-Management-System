package com.windsnow1025.tourmanagementsystem;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class JDBCHelper extends DatabaseHelper {
    private static final String CREATE_TABLE_总公司 = """
                CREATE TABLE IF NOT EXISTS 总公司
                (
                    id INT NOT NULL AUTO_INCREMENT,
                    总公司名 VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
            """;

    private static final String CREATE_TABLE_旅游分公司 = """
                CREATE TABLE IF NOT EXISTS 旅游分公司
                (
                    id        INT          NOT NULL AUTO_INCREMENT,
                    分公司名  VARCHAR(255) NOT NULL,
                    总公司_id INT          NOT NULL,
                    PRIMARY KEY (id),
                    FOREIGN KEY (总公司_id) REFERENCES 总公司 (id)
                );
            """;

    private static final String CREATE_TABLE_身份信息 = """
                CREATE TABLE IF NOT EXISTS 身份信息
                (
                    身份证号 VARCHAR(255) NOT NULL,
                    姓名     VARCHAR(255) NOT NULL,
                    工作单位 VARCHAR(255),
                    职业     VARCHAR(255),
                    PRIMARY KEY (身份证号)
                );
            """;

    private static final String CREATE_TABLE_经理 = """
                CREATE TABLE IF NOT EXISTS 经理
                (
                    经理号    INT          NOT NULL AUTO_INCREMENT,
                    身份证号  VARCHAR(255) NOT NULL,
                    分公司_id INT          NOT NULL,
                    PRIMARY KEY (经理号),
                    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
                    FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id)
                );
            """;

    private static final String CREATE_TABLE_导游员工 = """
                CREATE TABLE IF NOT EXISTS 导游员工
                (
                    导游号       INT          NOT NULL AUTO_INCREMENT,
                    身份证号     VARCHAR(255) NOT NULL,
                    导游资格等级 VARCHAR(255) NOT NULL,
                    业绩金额     FLOAT        NOT NULL,
                    分公司_id    INT          NOT NULL,
                    旅游团_id    INT          NOT NULL,
                    PRIMARY KEY (导游号),
                    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
                    FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id)
                );
            """;

    private static final String CREATE_TABLE_旅游团 = """
                CREATE TABLE IF NOT EXISTS 旅游团
                (
                    id INT NOT NULL AUTO_INCREMENT,
                    PRIMARY KEY (id)
                );
            """;

    private static final String CREATE_TABLE_顾客 = """
                CREATE TABLE IF NOT EXISTS 顾客
                (
                    身份证号  VARCHAR(255) NOT NULL,
                    消费金额  FLOAT        NOT NULL,
                    旅游团_id INT          NOT NULL,
                    PRIMARY KEY (身份证号),
                    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
                    FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id)
                );
            """;

    private static final String CREATE_TABLE_旅游线路 = """
                CREATE TABLE IF NOT EXISTS 旅游线路
                (
                    id        INT NOT NULL AUTO_INCREMENT,
                    总公司_id INT NOT NULL,
                    PRIMARY KEY (id)
                );
            """;

    private static final String CREATE_TABLE_旅游信息 = """
                CREATE TABLE IF NOT EXISTS 旅游信息
                (
                    id            INT            NOT NULL AUTO_INCREMENT,
                    旅游时间      VARCHAR(255)   NOT NULL,
                    旅游线路      VARCHAR(255)   NOT NULL,
                    旅游费用      FLOAT          NOT NULL,
                    保险          VARCHAR(255)   NOT NULL,
                    服务等级      VARCHAR(255)   NOT NULL,
                    旅游合同      BLOB,
                    旅游团_id     INT            NOT NULL,
                    顾客_身份证号 VARCHAR(255)   NOT NULL,
                    旅游线路_id   INT            NOT NULL,
                    PRIMARY KEY (id),
                    FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id),
                    FOREIGN KEY (顾客_身份证号) REFERENCES 顾客 (身份证号),
                    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
                );
            """;

    private static final String CREATE_TABLE_地点 = """
                CREATE TABLE IF NOT EXISTS 地点
                (
                    地点        VARCHAR(255) NOT NULL,
                    旅游线路_id INT          NOT NULL,
                    PRIMARY KEY (地点, 旅游线路_id),
                    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
                );
            """;

    private static final String CREATE_TABLE_景点 = """
                CREATE TABLE IF NOT EXISTS 景点
                (
                    景点        VARCHAR(255) NOT NULL,
                    旅游线路_id INT          NOT NULL,
                    PRIMARY KEY (景点, 旅游线路_id),
                    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
                );
            """;

    private static final String CREATE_TABLE_旅游时间段 = """
                CREATE TABLE IF NOT EXISTS 旅游时间段
                (
                    旅游时间段  VARCHAR(255) NOT NULL,
                    价格        FLOAT        NOT NULL,
                    交通方式    VARCHAR(255) NOT NULL,
                    服务等级    VARCHAR(255) NOT NULL,
                    收入信息    FLOAT        NOT NULL,
                    旅游线路_id INT          NOT NULL,
                    PRIMARY KEY (旅游时间段, 旅游线路_id),
                    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
                );
            """;


    public JDBCHelper() {
        super();
    }

    @Override
    protected void setDatabaseConfig() {
        try (InputStream inputStream = JDBCHelper.class.getClassLoader().getResourceAsStream("config.json")) {
            String text = new String(inputStream.readAllBytes());
            JSONObject jsonObject = new JSONObject(text);
            dbUrl = jsonObject.getString("database_url");
            dbUsername = jsonObject.getString("database_username");
            dbPassword = jsonObject.getString("database_password");
            dbDriverClassName = "com.mysql.cj.jdbc.Driver";
            dbVersion = "1.0";
        } catch (IOException e) {
            logger.error("Database config failed", e);
        }
    }

    @Override
    public void onCreate() throws SQLException {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(CREATE_TABLE_总公司);
            statement.executeUpdate(CREATE_TABLE_旅游分公司);
            statement.executeUpdate(CREATE_TABLE_身份信息);
            statement.executeUpdate(CREATE_TABLE_经理);
            statement.executeUpdate(CREATE_TABLE_导游员工);
            statement.executeUpdate(CREATE_TABLE_旅游团);
            statement.executeUpdate(CREATE_TABLE_顾客);
            statement.executeUpdate(CREATE_TABLE_旅游线路);
            statement.executeUpdate(CREATE_TABLE_旅游信息);
            statement.executeUpdate(CREATE_TABLE_地点);
            statement.executeUpdate(CREATE_TABLE_景点);
            statement.executeUpdate(CREATE_TABLE_旅游时间段);
        }

        createMetadata();
        insertVersion();
        logger.info("Database created");
    }

    @Override
    public void onUpgrade() throws SQLException {
        try (Statement statement = getConnection().createStatement()) {
            // Drop all
            statement.executeUpdate("DROP TABLE IF EXISTS 总公司");
            statement.executeUpdate("DROP TABLE IF EXISTS 旅游分公司");
            statement.executeUpdate("DROP TABLE IF EXISTS 身份信息");
            statement.executeUpdate("DROP TABLE IF EXISTS 经理");
            statement.executeUpdate("DROP TABLE IF EXISTS 导游员工");
            statement.executeUpdate("DROP TABLE IF EXISTS 旅游团");
            statement.executeUpdate("DROP TABLE IF EXISTS 顾客");
            statement.executeUpdate("DROP TABLE IF EXISTS 旅游信息");
            statement.executeUpdate("DROP TABLE IF EXISTS 旅游线路");
            statement.executeUpdate("DROP TABLE IF EXISTS 地点");
            statement.executeUpdate("DROP TABLE IF EXISTS 景点");
            statement.executeUpdate("DROP TABLE IF EXISTS 旅游时间段");

            // Create all
            statement.executeUpdate(CREATE_TABLE_总公司);
            statement.executeUpdate(CREATE_TABLE_旅游分公司);
            statement.executeUpdate(CREATE_TABLE_身份信息);
            statement.executeUpdate(CREATE_TABLE_经理);
            statement.executeUpdate(CREATE_TABLE_导游员工);
            statement.executeUpdate(CREATE_TABLE_旅游团);
            statement.executeUpdate(CREATE_TABLE_顾客);
            statement.executeUpdate(CREATE_TABLE_旅游信息);
            statement.executeUpdate(CREATE_TABLE_旅游线路);
            statement.executeUpdate(CREATE_TABLE_地点);
            statement.executeUpdate(CREATE_TABLE_景点);
            statement.executeUpdate(CREATE_TABLE_旅游时间段);
        }

        updateVersion();
        logger.info("Database upgraded");
    }
}
