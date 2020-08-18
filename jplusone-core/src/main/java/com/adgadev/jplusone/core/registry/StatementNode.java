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

package com.adgadev.jplusone.core.registry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.adgadev.jplusone.core.registry.StatementType.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StatementNode implements StatementNodeView {

    public static final String COLUMN_LIST_SUBSTITUTE = "#SELECT_COLUMNS_LIST";

    private static final String COMMENT_START_TOKEN = "/* ";

    private static final String COMMENT_END_TOKEN = " */ ";

    private static final String SELECT_CLAUSE = "select ";

    private static final String FROM_CLAUSE = " from ";

    private final String sql;

    private final StatementType statementType;

    public static StatementNode fromSql(String sql) {
        String sqlWithoutComments = stripComments(sql);
        StatementType type = StatementType.resolveStatementType(sqlWithoutComments);
        return new StatementNode(formatSql(sqlWithoutComments, type), type);
    }

    private static String stripComments(String sql) {
        if (sql.startsWith(COMMENT_START_TOKEN)) {
            int commentEndIndex = sql.indexOf(COMMENT_END_TOKEN);

            if (commentEndIndex > 0 && commentEndIndex + COMMENT_END_TOKEN.length() < sql.length()) {
                return sql.substring(commentEndIndex + COMMENT_END_TOKEN.length());
            }
        }

        return sql;
    }

    private static String formatSql(String sql, StatementType type) {
        if (type == SELECT) {
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
}
