package web_app.repository;

import web_app.repository.cache.cache_connectors.CacheConnector;
import web_app.repository.cache.cache_managers.CacheManager;
import web_app.repository.db.db_connectors.Connector;
import web_app.repository.db.db_managers.ConnectorManager;
import web_app.repository.db.db_models.ResultModel;
import web_app.repository.exceptions.CacheConnectionException;
import web_app.repository.exceptions.NoDataInCacheException;
import web_app.repository.exceptions.NoDataInDBException;

import java.io.IOException;
import java.util.List;

public class DataRepository {

    private static final String VALUE_KEY = "VALUE_PREFIX_KEY_%s";

    private ConnectorManager connectorManager;
    private CacheManager cacheManager;

    public DataRepository(ConnectorManager connectorManager, CacheManager cacheManager) {
        this.connectorManager = connectorManager;
        this.cacheManager = cacheManager;
    }

    public boolean insertResultModel(ResultModel model) throws Exception {
        try (Connector connector = connectorManager.getConnector()) {
            return connector.insertResultModel(model);
        }
    }

    public int insertRequestedValue (String value) throws Exception {
        try (Connector connector = connectorManager.getConnector()) {
            return connector.insertRequestedValue(value);
        }
    }

    public List<ResultModel> getResultByValue (String value) {
        List<ResultModel> resultModels = null;
        try {
            return retrieveFromCache(value);
        } catch (NoDataInCacheException e) {
            try {
                resultModels = retrieveFromDatabase(value);
                putModelsListToCache(value, resultModels);
            } catch (NoDataInDBException | CacheConnectionException ex ) {
                ex.printStackTrace();
            }
        } catch (CacheConnectionException e) {
            e.printStackTrace();
            try {
                resultModels = retrieveFromDatabase(value);
            } catch (NoDataInDBException ex ) {
                ex.printStackTrace();
            }
        }

        return resultModels;
    }

    public ResultModel getResultById(int id) {
        ResultModel resultModel = null;
        try {
            return retrieveFromCache(id);
        } catch (NoDataInCacheException e) {
            try {
                resultModel = retrieveFromDatabase(id);
                putModelToCache(id, resultModel);
            } catch (NoDataInDBException | CacheConnectionException ex ) {
                ex.printStackTrace();
            }
        } catch (CacheConnectionException e) {
            e.printStackTrace();
            try {
                resultModel = retrieveFromDatabase(id);
            } catch (NoDataInDBException ex ) {
                ex.printStackTrace();
            }
        }

        return resultModel;
    }

    private ResultModel retrieveFromDatabase(int id) throws NoDataInDBException {
        try (Connector connector = connectorManager.getConnector()) {
            ResultModel model = connector.getResultById(id);
            if (model != null) {
                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new NoDataInDBException();
    }

    private List<ResultModel> retrieveFromDatabase(String value) throws NoDataInDBException {
        try (Connector connector = connectorManager.getConnector()) {
            List<ResultModel> resultModels = connector.getResultByValue(value);

            if (resultModels != null) {
                return resultModels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new NoDataInDBException();
    }

    private List<ResultModel> retrieveFromCache(String value) throws CacheConnectionException, NoDataInCacheException {
        try (CacheConnector cacheConnector = cacheManager.getCacheConnector()) {
            List<ResultModel> resultModels = cacheConnector.getResultModelList(String.format(VALUE_KEY, value));

            if (resultModels != null) {
                return resultModels;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                throw new CacheConnectionException();
            }
        }

        throw new NoDataInCacheException();
    }

    private ResultModel retrieveFromCache(int id) throws NoDataInCacheException, CacheConnectionException {
        try (CacheConnector cacheConnector = cacheManager.getCacheConnector()) {
            ResultModel model = cacheConnector.getResultModel(id);

            if (model != null) {
                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                throw new CacheConnectionException();
            }
        }

        throw new NoDataInCacheException();
    }

    private void putModelToCache(int id, ResultModel model) throws CacheConnectionException {
        try (CacheConnector cacheConnector = cacheManager.getCacheConnector()) {
            cacheConnector.addResultModel(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CacheConnectionException();
        }
    }

    private void putModelsListToCache(String key, List<ResultModel> models) throws CacheConnectionException {
        try (CacheConnector cacheConnector = cacheManager.getCacheConnector()) {

            cacheConnector.addResultModelList(String.format(VALUE_KEY, key), models);

            for (ResultModel model : models) {
                cacheConnector.addResultModel(model.getId(), model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CacheConnectionException();
        }
    }
}
