package com.grexdev.nplusone.core.proxy.datasource;

import com.grexdev.nplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@RequiredArgsConstructor
class StatementProxy implements Statement {

    @Delegate(excludes = StatementOverwrite.class)
    private final Statement delegate;

    private final StateListener stateListener;

    @Override
    public boolean execute(String sql) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.execute(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.execute(sql, columnNames);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        stateListener.statementExecuted(sql);
        return delegate.executeUpdate(sql, columnNames);
    }

    private interface StatementOverwrite {

        ResultSet executeQuery(String sql) throws SQLException;

        boolean execute(String sql) throws SQLException;

        boolean execute(String sql, int autoGeneratedKeys) throws SQLException;

        boolean execute(String sql, int columnIndexes[]) throws SQLException;

        boolean execute(String sql, String columnNames[]) throws SQLException;

        int executeUpdate(String sql) throws SQLException;

        int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException;

        int executeUpdate(String sql, int columnIndexes[]) throws SQLException;

        int executeUpdate(String sql, String columnNames[]) throws SQLException;
    }
}