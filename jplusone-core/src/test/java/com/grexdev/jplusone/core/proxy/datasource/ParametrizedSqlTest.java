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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParametrizedSqlTest {

    // TODO: replace with spock
    @Test
    void forSql() {
        ParametrizedSql parametrizedSql = ParametrizedSql.forSql("SELECT id FROM table t WHERE t.int_column = ? AND t.str_column = ?");
        parametrizedSql.setInt(1, 100);
        parametrizedSql.setString(2, "abc");

        assertEquals("SELECT id FROM table t WHERE t.int_column = 100 AND t.str_column = 'abc'", parametrizedSql.getSqlWithParameters());
    }

}