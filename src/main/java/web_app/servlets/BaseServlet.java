package web_app.servlets;

import org.ini4j.Ini;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.common.Utils;
import web_app.repository.DataRepository;
import web_app.repository.cache.cache_managers.MemcachedManager;
import web_app.repository.db.db_managers.MySQLConnectorManager;
import web_app.repository.repository_types.CachedRepository;
import web_app.repository.repository_types.NonCachedRepository;
import web_app.repository.repository_types.Repository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class BaseServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);

    private static final String[] jdbcParams = new String[] {
            Constants.JDBC_URL,
            Constants.JDBC_USER,
            Constants.JDBC_PASSWORD
    };

    private static final String[] activeMqParams = new String[] {
            Constants.ACTIVE_MQ_BROKER_URL,
            Constants.ACTIVE_MQ_PRODUCER_ID,
            Constants.ACTIVE_MQ_QUEUE_NAME
    };

    private static final String[] cacheParams = new String[] {
            Constants.CACHE_USE_CACHE,
            Constants.CACHE_HOST,
            Constants.CACHE_PORT,
            Constants.CACHE_TIMEOUT,
            Constants.CACHE_EXPIRATION_TIME
    };

    protected DataRepository dataRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Properties properties = getConfigProperties();

            initDataRepositoryByConfig(properties);

            getServletContext().setAttribute(Constants.CONFIG_PROPERTIES_ATTRIBUTE_NAME, properties);

        } catch (IOException ex) {
            logger.error("Config file {} not found.",
                    System.getenv(getServletContext().getInitParameter(Constants.ENV_VAR_NAME)), ex);
        }
    }

    private void initDataRepositoryByConfig (@NotNull Properties properties) {

        Repository repositoryType;

        String useCache = properties.getProperty(Constants.CACHE_USE_CACHE);

        if (useCache != null && useCache.equalsIgnoreCase(Constants.USE_CACHE_VALUE)) {
            MemcachedManager memcachedManager = new MemcachedManager.Builder()
                    .setHost(properties.getProperty(Constants.CACHE_HOST))
                    .setPort(Integer.parseInt(properties.getProperty(Constants.CACHE_PORT)))
                    .setOperationTimeoutMillis(Integer.parseInt(properties.getProperty(Constants.CACHE_TIMEOUT)))
                    .setExpirationTimeMillis(Integer.parseInt(properties.getProperty(Constants.CACHE_EXPIRATION_TIME)))
                    .build();

            repositoryType = new CachedRepository(
                    new MySQLConnectorManager(properties),
                    memcachedManager);
        } else {
            repositoryType = new NonCachedRepository(
                    new MySQLConnectorManager(properties));

        }

        dataRepository = new DataRepository();
        dataRepository.setRepositoryType(repositoryType);
    }

    @NotNull
    private Properties getConfigProperties() throws IOException {
        String filename = System.getenv(getServletContext().getInitParameter(Constants.ENV_VAR_NAME));
        Reader configFileReader = new FileReader(filename);

        Properties properties = new Properties();

        Ini configFile = new Ini();
        configFile.load(configFileReader);

        Utils.addConfigParams(configFile, Constants.JDBC_CFG, jdbcParams, properties);
        Utils.addConfigParams(configFile, Constants.ACTIVE_MQ_CFG, activeMqParams, properties);
        Utils.addConfigParams(configFile, Constants.CACHE_CFG, cacheParams, properties);

        return properties;
    }
}
