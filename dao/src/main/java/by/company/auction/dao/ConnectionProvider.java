package by.company.auction.dao;

import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class ConnectionProvider {

    private PGPoolingDataSource dataSource = new PGPoolingDataSource();

    private ConnectionProvider() {

        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("auction_database");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");

    }

    Connection getConnection() throws SQLException {

        return dataSource.getConnection();

    }

    private static ConnectionProvider connectionProviderInstance;

    static ConnectionProvider getInstance() {
        if (connectionProviderInstance != null) {
            return connectionProviderInstance;
        }
        connectionProviderInstance = new ConnectionProvider();
        return connectionProviderInstance;
    }
}
