package com.windsnow1025.tourmanagementsystem;
import java.sql.*;

public class JDBCHelper extends DatabaseHelper{
    private static final String CREATE_TABLE_总公司 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '总公司')
        BEGIN
            CREATE TABLE 总公司
            (
                id INT NOT NULL IDENTITY,
                总公司名 VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )
        END
        """;

    private static final String CREATE_TABLE_旅游分公司 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '旅游分公司')
        BEGIN
            CREATE TABLE 旅游分公司
            (
                id        INT          NOT NULL IDENTITY,
                分公司名  VARCHAR(255) NOT NULL,
                总公司_id INT          NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (总公司_id) REFERENCES 总公司 (id)
            )
        END
        """;

    private static final String CREATE_TABLE_身份信息 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '身份信息')
        BEGIN
            CREATE TABLE 身份信息
            (
                身份证号 VARCHAR(255) NOT NULL,
                姓名     VARCHAR(255) NOT NULL,
                工作单位 VARCHAR(255),
                职业     VARCHAR(255),
                PRIMARY KEY (身份证号)
            )
        END
        """;

    private static final String CREATE_TABLE_经理 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '经理')
        BEGIN
            CREATE TABLE 经理
            (
                经理号    INT          NOT NULL IDENTITY,
                身份证号  VARCHAR(255) NOT NULL,
                分公司_id INT          NOT NULL,
                PRIMARY KEY (经理号),
                FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
                FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id)
            )
        END
        """;

    private static final String CREATE_TABLE_导游员工 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '导游员工')
        BEGIN
            CREATE TABLE 导游员工
            (
                导游号       INT          NOT NULL IDENTITY,
                身份证号     VARCHAR(255) NOT NULL,
                导游资格等级 VARCHAR(255) NOT NULL,
                业绩金额     FLOAT        NOT NULL,
                分公司_id    INT          NOT NULL,
                旅游团_id    INT          NOT NULL,
                PRIMARY KEY (导游号),
                FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
                FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id)
            )
        END
        """;

    private static final String CREATE_TABLE_旅游团 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '旅游团')
        BEGIN
            CREATE TABLE 旅游团
            (
                id INT NOT NULL IDENTITY,
                PRIMARY KEY (id)
            )
        END
        """;

    private static final String CREATE_TABLE_顾客 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '顾客')
        BEGIN
            CREATE TABLE 顾客
            (
                身份证号  VARCHAR(255) NOT NULL,
                消费金额  FLOAT        NOT NULL,
                旅游团_id INT          NOT NULL,
                PRIMARY KEY (身份证号),
                FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
                FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id)
            )
        END
        """;

    private static final String CREATE_TABLE_旅游信息 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '旅游信息')
        BEGIN
            CREATE TABLE 旅游信息
            (
                id            INT            NOT NULL IDENTITY,
                旅游时间      VARCHAR(255)   NOT NULL,
                旅游线路      VARCHAR(255)   NOT NULL,
                旅游费用      FLOAT          NOT NULL,
                保险          VARCHAR(255)   NOT NULL,
                服务等级      VARCHAR(255)   NOT NULL,
                旅游合同      VARBINARY(MAX) NOT NULL,
                旅游团_id     INT            NOT NULL,
                顾客_身份证号 VARCHAR(255)   NOT NULL,
                旅游线路_id   INT            NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id),
                FOREIGN KEY (顾客_身份证号) REFERENCES 顾客 (身份证号),
                FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
            )
        END
        """;

    private static final String CREATE_TABLE_旅游线路 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '旅游线路')
        BEGIN
            CREATE TABLE 旅游线路
            (
                id        INT NOT NULL IDENTITY,
                总公司_id INT NOT NULL,
                PRIMARY KEY (id)
            )
        END
        """;

    private static final String CREATE_TABLE_地点 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '地点')
        BEGIN
            CREATE TABLE 地点
            (
                地点        VARCHAR(255) NOT NULL,
                旅游线路_id INT          NOT NULL,
                PRIMARY KEY (地点, 旅游线路_id),
                FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
            )
        END
        """;

    private static final String CREATE_TABLE_景点 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '景点')
        BEGIN
            CREATE TABLE 景点
            (
                景点        VARCHAR(255) NOT NULL,
                旅游线路_id INT          NOT NULL,
                PRIMARY KEY (景点, 旅游线路_id),
                FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
            )
        END
        """;

    private static final String CREATE_TABLE_旅游时间段 = """
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '旅游时间段')
        BEGIN
            CREATE TABLE 旅游时间段
            (
                旅游时间段  VARCHAR(255) NOT NULL,
                价格        FLOAT        NOT NULL,
                交通方式    VARCHAR(255) NOT NULL,
                服务等级    VARCHAR(255) NOT NULL,
                收入信息    FLOAT        NOT NULL,
                旅游线路_id INT          NOT NULL,
                PRIMARY KEY (旅游时间段, 旅游线路_id),
                FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
            )
        END
        """;



    public JDBCHelper() {
        super();
    }
    @Override
    protected void setDatabaseConfig() {
        dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=tour";
        dbUsername = "tour_user";
        dbPassword = "tour_password";
        dbDriverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
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
            statement.executeUpdate(CREATE_TABLE_旅游信息);
            statement.executeUpdate(CREATE_TABLE_旅游线路);
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
