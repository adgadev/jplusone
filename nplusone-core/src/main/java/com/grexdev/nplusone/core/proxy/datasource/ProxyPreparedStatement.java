package com.grexdev.nplusone.core.proxy.datasource;

import com.grexdev.nplusone.core.proxy.StateListener;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Wrapper;

@Slf4j
public class ProxyPreparedStatement implements PreparedStatement {

    @Delegate(types = {PreparedStatement.class, Wrapper.class}, excludes = PreparedStatementOverwrite.class)
    private final PreparedStatement delegate;

    private final ProxyContext context;

    private final StateListener stateListener;

    private final ParametrizedSql parametrizedSql;

    public ProxyPreparedStatement(PreparedStatement delegate, ProxyContext context, StateListener stateListener, String sql) {
        this.delegate = delegate;
        this.context = context;
        this.stateListener = stateListener;
        this.parametrizedSql = ParametrizedSql.forSql(sql);
    }

    @Override
    public void clearParameters() throws SQLException {
        parametrizedSql.clearParameters();
        delegate.clearParameters();
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        parametrizedSql.setInt(parameterIndex, x);
        delegate.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        parametrizedSql.setLong(parameterIndex, x);
        delegate.setLong(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        parametrizedSql.setString(parameterIndex, x);
        delegate.setString(parameterIndex, x);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        if (context.isRecording()) {
            stateListener.statementExecuted(parametrizedSql.getSqlWithParameters());
        }
        return delegate.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        if (context.isRecording()) {
            stateListener.statementExecuted(parametrizedSql.getSqlWithParameters());
        }
        return delegate.executeUpdate();
    }

    @Override
    public boolean execute() throws SQLException {
        if (context.isRecording()) {
            stateListener.statementExecuted(parametrizedSql.getSqlWithParameters());
        }
        return delegate.execute();
    }

    private interface PreparedStatementOverwrite {

        ResultSet executeQuery() throws SQLException;

        int executeUpdate() throws SQLException;

        boolean execute() throws SQLException;

        void setString(int parameterIndex, String x) throws SQLException;

        void setInt(int parameterIndex, int x) throws SQLException;

        void setLong(int parameterIndex, long x) throws SQLException;

        // TODO: implement otrher setXXX

        void clearParameters() throws SQLException;

    }

}
