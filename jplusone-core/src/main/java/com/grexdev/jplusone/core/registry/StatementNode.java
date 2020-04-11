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

package com.grexdev.jplusone.core.registry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.grexdev.jplusone.core.registry.StatementNode.StatementType.READ;
import static com.grexdev.jplusone.core.registry.StatementNode.StatementType.WRITE;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StatementNode {

    private static final String SELECT_CLAUSE = "select ";

    private static final String FROM_CLAUSE = " from ";

    private static final String COLUMN_LIST_SUBSTITUTE = "[...]";

    public enum StatementType { READ, WRITE }

    private final String sql;

    private final StatementType statementType;

    public static StatementNode fromSql(String sql) {
        StatementType type = sql.startsWith(SELECT_CLAUSE) ? READ : WRITE;
        return new StatementNode(formatSql(sql, type), type);
    }

    private static String formatSql(String sql, StatementType type) {
        if (type == READ) {
            int index = sql.indexOf(FROM_CLAUSE);

            if (index >= 0) {
                return new StringBuilder()
                        .append(SELECT_CLAUSE)
                        .append(COLUMN_LIST_SUBSTITUTE)
                        .append(sql.substring(index))
                        .toString();
            }
        }

        return sql;
    }

    // TODO: format JOIN statements

}
