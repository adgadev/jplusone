/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grexdev.jplusone.core.proxy.datasource;

import com.grexdev.jplusone.core.proxy.StateListener;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Wrapper;

@Slf4j
class PreparedStatementProxy implements PreparedStatement {

    @Delegate(types = {PreparedStatement.class, Wrapper.class}, excludes = PreparedStatementOverwrite.class)
    private final PreparedStatement delegate;

    private final StateListener stateListener;

    private final ParametrizedSql parametrizedSql;

    PreparedStatementProxy(PreparedStatement delegate, StateListener stateListener, String sql) {
        this.delegate = delegate;
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
        trackStatementExecution();
        return delegate.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        trackStatementExecution();
        return delegate.executeUpdate();
    }

    @Override
    public boolean execute() throws SQLException {
        trackStatementExecution();
        return delegate.execute();
    }

    private void trackStatementExecution() {
        stateListener.statementExecuted(parametrizedSql::getSqlWithParameters);
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
