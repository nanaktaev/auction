package by.company.auction.dao;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class ConnectionProvider {

    private static ConnectionProvider connectionProviderInstance;

    private ConnectionProvider() {
    }

    Connection getConnection() throws SQLException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("auction_database");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");

        return dataSource.getConnection();
    }

    static ConnectionProvider getInstance() {
        if (connectionProviderInstance != null) {
            return connectionProviderInstance;
        }
        connectionProviderInstance = new ConnectionProvider();
        return connectionProviderInstance;
    }
}
