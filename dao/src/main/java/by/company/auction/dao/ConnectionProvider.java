package by.company.auction.dao;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {

    private static ConnectionProvider connectionProviderInstance;

    private ConnectionProvider() {
    }

    Connection getConnection() throws SQLException {

        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setServerName("localhost");
        dataSource.setPortNumber(3306);
        dataSource.setDatabaseName("auction_database");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setServerTimezone("Europe/Moscow");

        return dataSource.getConnection();
    }

    public static ConnectionProvider getInstance() {
        if (connectionProviderInstance != null) {
            return connectionProviderInstance;
        }
        connectionProviderInstance = new ConnectionProvider();
        return connectionProviderInstance;
    }
}
