package com.grexdev.nplusone.core.proxy.datasource;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class ProxyDataSource implements DataSource {

    @Delegate(excludes = DataSourceOverwrite.class)
    private final DataSource delegate;

    private final ProxyContext context;

    public Connection getConnection() throws SQLException {
        Connection connection = delegate.getConnection();
        return new ProxyConnection(connection, context);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = delegate.getConnection(username, password);
        return new ProxyConnection(connection, context);
    }

    private interface DataSourceOverwrite {

        Connection getConnection() throws SQLException;

        Connection getConnection(String username, String password) throws SQLException;
    }
}
