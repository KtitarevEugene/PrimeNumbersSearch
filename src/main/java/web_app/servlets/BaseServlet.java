package web_app.servlets;

import com.mysql.jdbc.Driver;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.common.Utils;
import web_app.repository.DataRepository;
import web_app.repository.cache.cache_managers.CacheManager;
import web_app.repository.cache.cache_managers.MemcachedManager;
import web_app.repository.db.db_managers.ConnectorManager;
import web_app.repository.db.db_managers.MySQLConnectorManager;
import web_app.repository.repository_types.CachedRepository;
import web_app.repository.repository_types.NonCachedRepository;
import web_app.repository.repository_types.Repository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BaseServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);

    protected DataRepository dataRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Ini configFile = loadConfigFile();

            initDataRepository(configFile);

            saveConfigPropertiesInContext(configFile);

        } catch (IOException ex) {
            logger.error("Config file {} not found.",
                    System.getenv(getServletContext().getInitParameter(Constants.ENV_VAR_NAME)), ex);
        } catch (SQLException ex) {
            logger.error("SQLException has been thrown: ", ex);
        }
    }

    @NotNull
    private Ini loadConfigFile() throws IOException {
        String filename = System.getenv(getServletContext().getInitParameter(Constants.ENV_VAR_NAME));
        Reader configFileReader = new FileReader(filename);

        Ini configFile = new Ini();
        configFile.load(configFileReader);

        return configFile;
    }

    private void initDataRepository (@NotNull Ini configFile) throws SQLException {
        Properties jdbcProps = getPropertiesBySectionName(configFile, Constants.JDBC_CFG);
        Properties cacheProps = getPropertiesBySectionName(configFile, Constants.CACHE_CFG);

        Repository repositoryType = getRepositoryType(cacheProps, jdbcProps);

        dataRepository = new DataRepository();
        dataRepository.setRepositoryType(repositoryType);
    }

    @NotNull
    private Properties getPropertiesBySectionName(Ini configFile, String sectionName) {
        Properties properties = new Properties();

        Utils.addAllConfigParamsFromSection(configFile, sectionName, properties);

        return properties;
    }

    @NotNull
    private Repository getRepositoryType(@NotNull Properties cacheProps, @NotNull Properties jdbcProps) throws SQLException {

        String useCache = cacheProps.getProperty(Constants.CACHE_USE_CACHE);

        if (useCache != null && useCache.equalsIgnoreCase(Constants.USE_CACHE_VALUE)) {
            CacheManager memcachedManager = new MemcachedManager.Builder()
                    .setHost(cacheProps.getProperty(Constants.CACHE_HOST))
                    .setPort(Integer.parseInt(cacheProps.getProperty(Constants.CACHE_PORT)))
                    .setOperationTimeoutMillis(Integer.parseInt(cacheProps.getProperty(Constants.CACHE_TIMEOUT)))
                    .setExpirationTimeMillis(Integer.parseInt(cacheProps.getProperty(Constants.CACHE_EXPIRATION_TIME)))
                    .build();

            return new CachedRepository(
                    getConnectorManager(jdbcProps),
                    memcachedManager);
        }

        return new NonCachedRepository(
                getConnectorManager(jdbcProps));

    }

    @NotNull
    private ConnectorManager getConnectorManager(@NotNull Properties properties) throws SQLException {
        DriverManager.registerDriver(new Driver());

        MySQLConnectorManager.Builder builder = new MySQLConnectorManager.Builder()
                .setUrl(properties.getProperty(Constants.JDBC_URL))
                .setUsername(properties.getProperty(Constants.JDBC_USER))
                .setPassword(properties.getProperty(Constants.JDBC_PASSWORD))
                .setConnectionTestQuery(properties.getProperty(Constants.JDBC_CONNECTION_TEXT_QUERY))
                .setPoolName(properties.getProperty(Constants.JDBC_POOL_NAME));
        try {
            builder.setLeakDetectionThreshold(Integer.parseInt(properties.getProperty(Constants.JDBC_LEAK_DETECTION_THRESHOLD)));
        } catch (NumberFormatException ex) {
            logger.error("can't set {} parameter, used default value", Constants.JDBC_LEAK_DETECTION_THRESHOLD);
        }

        try {
            builder.setMaximumPoolSize(Integer.parseInt(properties.getProperty(Constants.JDBC_MAXIMUM_POOL_SIZE)));
        } catch (NumberFormatException ex) {
            logger.error("can't set {} parameter, used default value", Constants.JDBC_MAXIMUM_POOL_SIZE);
        }

        try {
            builder.setMinimumIdle(Integer.parseInt(properties.getProperty(Constants.JDBC_MINIMUM_IDLE)));
        } catch (NumberFormatException ex) {
            logger.error("can't set {} parameter, used default value", Constants.JDBC_LEAK_DETECTION_THRESHOLD);
        }

        for (Object obj : properties.keySet()) {
            if (obj instanceof String) {
                String key = (String) obj;
                if (key.startsWith("dataSource.")) {
                    builder.addSourceProperty(key, properties.getProperty(key));
                }
            }
        }

        return builder.build();
    }

    private void saveConfigPropertiesInContext(@NotNull Ini configFile) {

        Properties properties = new Properties();

        for (String sectionKey : configFile.keySet()) {
            Profile.Section section = configFile.get(sectionKey);
            if (section != null) {
                for (String propKey : section.keySet()) {
                    properties.setProperty(propKey, section.get(propKey));
                }
            }
        }

        getServletContext().setAttribute(Constants.CONFIG_PROPERTIES_ATTRIBUTE_NAME, properties);

    }
}
