package web_app.repository.db.db_managers;

import com.mysql.jdbc.Driver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.repository.db.db_connectors.Connector;
import web_app.repository.db.db_connectors.MySQLConnector;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnectorManager implements ConnectorManager {

    private final Logger logger = LoggerFactory.getLogger(MySQLConnectorManager.class);

    private HikariDataSource dataSource;

    private String url = "jdbc:mysql://localhost:3306/results";
    private String userName = "root";
    private String password = "root";
    private int poolSize = 10;

    public MySQLConnectorManager(Properties properties) {

        String connectionStr = getProperty(properties, Constants.JDBC_URL);
        if (connectionStr != null) {
            url = connectionStr;
        }

        String user = getProperty(properties, Constants.JDBC_USER);
        if (user != null) {
            userName = user;
        }

        String pass = getProperty(properties, Constants.JDBC_PASSWORD);
        if (pass != null) {
            password = pass;
        }

        String poolSizeProperty = getProperty(properties, Constants.JDBC_POOL_SIZE);
        if (poolSizeProperty != null) {
            try {
                this.poolSize = Integer.parseInt(poolSizeProperty);
            } catch (NumberFormatException ex) {
                logger.error("can't set connection pool size used default value");
            }
        }

        init();
    }

    public MySQLConnectorManager(String url, String userName, String password) {

        this.url = url;
        this.userName = userName;
        this.password = password;

        init();
    }

    private String getProperty(Properties properties, String name) {
        String value = properties.getProperty(name);
        if (value == null) {
            logger.warn("Missing config property '{}'. Used default value", name);
        }

        return value;
    }

    private void init() {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            logger.error("SQL exception has been thrown.", e);
        }

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);

        dataSource = new HikariDataSource(config);
    }

    @Override
    public Connector getConnector() throws SQLException {
        return new MySQLConnector(dataSource.getConnection());
    }
}
