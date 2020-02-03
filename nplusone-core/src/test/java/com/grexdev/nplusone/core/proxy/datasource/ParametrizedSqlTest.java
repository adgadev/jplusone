package com.grexdev.nplusone.core.proxy.datasource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParametrizedSqlTest {

    // TODO: replace with spock
    @Test
    void forSql() {
        ParametrizedSql parametrizedSql = ParametrizedSql.forSql("SELECT id FROM table t WHERE t.int_column = ? AND t.str_column = ?");
        parametrizedSql.setInt(1, 100);
        parametrizedSql.setString(2, "abc");

        assertEquals("SELECT id FROM table t WHERE t.int_column = 100 AND t.str_column = \"abc\"", parametrizedSql.getSqlWithParameters());
    }

}