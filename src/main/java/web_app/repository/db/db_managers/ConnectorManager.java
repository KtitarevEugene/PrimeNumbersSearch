package web_app.repository.db.db_managers;

import web_app.repository.db.db_connectors.Connector;

public interface ConnectorManager {
    Connector getConnector();
}
