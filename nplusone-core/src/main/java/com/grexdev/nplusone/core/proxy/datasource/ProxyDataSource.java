package com.grexdev.nplusone.core.proxy.datasource;

import com.grexdev.nplusone.core.proxy.StateListener;
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

    private final StateListener stateListener;

    public Connection getConnection() throws SQLException {
        return new ProxyConnection(delegate.getConnection(), context, stateListener);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return new ProxyConnection(delegate.getConnection(username, password), context, stateListener);
    }

    private interface DataSourceOverwrite {

        Connection getConnection() throws SQLException;

        Connection getConnection(String username, String password) throws SQLException;
    }
}
