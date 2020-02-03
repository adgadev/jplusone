package com.grexdev.nplusone.core.proxy.datasource;

import com.grexdev.nplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DataSourceProxy implements DataSource {

    @Delegate(excludes = DataSourceOverwrite.class)
    private final DataSource delegate;

    private final StateListener stateListener;

    public Connection getConnection() throws SQLException {
        Connection connection = delegate.getConnection();
        return new ConnectionProxy(connection, stateListener);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = delegate.getConnection(username, password);
        return new ConnectionProxy(connection, stateListener);
    }

    private interface DataSourceOverwrite {

        Connection getConnection() throws SQLException;

        Connection getConnection(String username, String password) throws SQLException;
    }
}
