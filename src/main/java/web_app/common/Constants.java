package web_app.common;

import java.util.logging.Level;

public class Constants {

    public static final String ENV_VAR_NAME = "ENV_VAR_NAME";

    public static final String JDBC_CFG = "jdbc_cfg";
    public static final String JDBC_DRIVER_CLASS_NAME = "driverClassName";
    public static final String JDBC_URL = "url";
    public static final String JDBC_USER = "user";
    public static final String JDBC_PASSWORD = "password";
    public static final String JDBC_MAXIMUM_POOL_SIZE = "maximumPoolSize";
    public static final String JDBC_MINIMUM_IDLE = "minimumIdle";
    public static final String JDBC_POOL_NAME = "poolName";
    public static final String JDBC_CONNECTION_TEXT_QUERY = "connectionTestQuery";
    public static final String JDBC_LEAK_DETECTION_THRESHOLD = "leakDetectionThreshold";
    public static final String JDBC_USE_CONNECTION_POOL = "use_connection_pool";
    public static final String JDBC_PARAM_PREFIX = "param.";

    public static final String ACTIVE_MQ_CFG = "acivemq_cfg";
    public static final String ACTIVE_MQ_BROKER_URL = "broker_url";
    public static final String ACTIVE_MQ_PRODUCER_ID = "producer_id";
    public static final String ACTIVE_MQ_QUEUE_NAME = "queue_name";

    public static final String CACHE_CFG = "cache_cfg";
    public static final String CACHE_USE_CACHE = "use_cache";
    public static final String CACHE_HOST = "host";
    public static final String CACHE_PORT = "port";
    public static final String CACHE_EXPIRATION_TIME = "expiration_time";
    public static final String CACHE_TIMEOUT = "timeout";
    public static final String CACHE_LOG_LEVEL = "log_level";

    public static final String LOG_LEVEL_OFF = "OFF";
    public static final String LOG_LEVEL_SEVERE = "SEVERE";
    public static final String LOG_LEVEL_WARNING = "WARNING";
    public static final String LOG_LEVEL_INFO = "INFO";
    public static final String LOG_LEVEL_CONFIG = "CONFIG";
    public static final String LOG_LEVEL_FINE = "FINE";
    public static final String LOG_LEVEL_FINER = "FINER";
    public static final String LOG_LEVEL_FINEST = "FINEST";
    public static final String LOG_LEVEL_ALL = "ALL";

    public static final String USE_CACHE_VALUE = "true";
    public static final String USE_POOL_VALUE = "true";

    public static final String VALUE_FIELD = "value_field";
    public static final String VALUE_ID_PARAM = "value_id";

    public static final String VALUE_ATTRIBUTE_NAME = "fieldName";
    public static final String VALUE_ID_ATTRIBUTE_NAME = "value_id_attribute_name";
    public static final String REQUESTED_VALUE_ATTRIBUTE_NAME = "requested_value";
    public static final String VALUE_RESULT_ATTRIBUTE_NAME = "value_result_attribute_name";
    public static final String CONFIG_PROPERTIES_ATTRIBUTE_NAME = "config_properties_attribute_name";
}
