package com.windsnow1025.tourmanagementsystem.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public abstract class DatabaseHelper {
    protected static final Logger logger = Logger.getLogger(DatabaseHelper.class.getName());
    protected static HikariDataSource dataSource;
    protected static String dbUrl;
    protected static String dbUsername;
    protected static String dbPassword;
    protected static String dbDriverClassName;
    protected static String dbVersion;

    public DatabaseHelper() {
        try {
            setDatabaseConfig();

            // Data source
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUsername);
            config.setPassword(dbPassword);
            config.setDriverClassName(dbDriverClassName);
            dataSource = new HikariDataSource(config);

            // Version control
            String currentVersion = selectVersion();
            if (currentVersion == null) {
                onCreate();
            } else if (!currentVersion.equals(dbVersion)) {
                onUpgrade();
            }
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Database config failed", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public List<Map<String, Object>> select(String query, Object... params) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, params);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSetToList(resultSet);
            }
        }
    }

    public int executeUpdate(String query, Object... params) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, params);
            return preparedStatement.executeUpdate();
        }
    }

    // Set dbUrl, dbUsername, dbPassword, dbDriverClassName, dbVersion
    protected abstract void setDatabaseConfig();

    // To be overridden
    protected void onCreate() throws SQLException {
        createMetadata();
        insertVersion();
        logger.log(java.util.logging.Level.INFO, "Database created");
    }

    // To be overridden
    protected void onUpgrade() throws SQLException {
        updateVersion();
        logger.log(java.util.logging.Level.INFO, "Database upgraded");
    }

    protected void createMetadata() throws SQLException {
        final String CREATE_METADATA = """
                CREATE TABLE IF NOT EXISTS metadata (
                    version VARCHAR(255)
                );
                """;
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(CREATE_METADATA);
        }
    }

    protected void dropMetadata() throws SQLException {
        final String DROP_METADATA = "DROP TABLE IF EXISTS metadata";
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(DROP_METADATA);
        }
    }

    protected String selectVersion() {
        final String SELECT_METADATA = "SELECT version FROM metadata";
        try {
            List<Map<String, Object>> results = select(SELECT_METADATA);
            if (!results.isEmpty()) {
                return (String) results.get(0).get("version");
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Select version failed", e);
            return null;
        }
    }

    protected void insertVersion() throws SQLException {
        final String INSERT_METADATA = "INSERT INTO metadata (version) VALUES (?)";
        executeUpdate(INSERT_METADATA, dbVersion);
    }

    protected void updateVersion() throws SQLException {
        final String UPDATE_METADATA = "UPDATE metadata SET version = ?";
        executeUpdate(UPDATE_METADATA, dbVersion);
    }

    private void setParameters(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    private List<Map<String, Object>> resultSetToList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columns = resultSetMetaData.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

}