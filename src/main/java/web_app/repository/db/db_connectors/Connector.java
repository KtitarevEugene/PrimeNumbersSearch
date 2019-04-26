package web_app.repository.db.db_connectors;

import web_app.repository.db.db_models.ResultModel;

import java.util.List;

public interface Connector extends AutoCloseable {
    boolean insertResultModel(ResultModel model);
    int insertRequestedValue (String value);
    List<ResultModel> getResultByValue (String value);
    ResultModel getResultById(int id);
}
