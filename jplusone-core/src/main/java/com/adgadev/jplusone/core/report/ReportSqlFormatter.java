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

package com.adgadev.jplusone.core.report;

import com.adgadev.jplusone.core.registry.StatementNode;

import static org.hibernate.engine.jdbc.internal.FormatStyle.BASIC;

class ReportSqlFormatter {

    private static final String SELECT_CLAUSE_CAPTURED = "select " + StatementNode.COLUMN_LIST_SUBSTITUTE + " from";

    private static final String SELECT_CLAUSE_FORMATTED = "select [...] from";

    private static final String WHERE_CLAUSE_LINE = "    where";

    private static String INTEND = "    ";

    private static int SPACES_PER_INDENT = 4;

    private static int BASIC_FORMATTER_LEADING_SPACES = 4;

    static String formatSql(String generalIntend, String sql) {
        String formattedSql = BASIC.getFormatter().format(sql);
        String[] lines = formattedSql.split("\n");

        return sql.startsWith(SELECT_CLAUSE_CAPTURED)
                ? formatSelectSqlStatement(generalIntend, lines)
                : formatGenericSqlStatement(generalIntend, lines);
    }

    private static String formatGenericSqlStatement(String generalIntend, String[] lines) {
        StringBuilder builder = new StringBuilder();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            builder.append('\n');
            builder.append(generalIntend);
            builder.append(line.substring(BASIC_FORMATTER_LEADING_SPACES));
        }

        return builder.toString();
    }

    static String formatSelectSqlStatement(String generalIntend, String[] lines) {
        StringBuilder builder = new StringBuilder();
        boolean whereClauseProcessed = false;

        builder.append('\n');
        builder.append(generalIntend);
        builder.append(SELECT_CLAUSE_FORMATTED);

        String fromTableName = lines[4].substring(BASIC_FORMATTER_LEADING_SPACES);
        builder.append('\n');
        builder.append(generalIntend);
        builder.append(fromTableName);

        for (int i = 5; i < lines.length; i++) {
            String line = lines[i];
            int leadingSpaces = countLeadingSpaces(line);
            int indentationDepth = leadingSpaces / SPACES_PER_INDENT - 1;

            if (line.equals(WHERE_CLAUSE_LINE)) {
                whereClauseProcessed = true;
            }

            if (!whereClauseProcessed) {
                if (indentationDepth >= 1) {
                    builder.append(" ");
                    builder.append(line.trim());
                } else {
                    builder.append('\n');
                    builder.append(generalIntend);
                    builder.append(INTEND);
                    builder.append(line.substring(BASIC_FORMATTER_LEADING_SPACES));
                }
            } else {
                builder.append('\n');
                builder.append(generalIntend);
                builder.append(line.substring(BASIC_FORMATTER_LEADING_SPACES));
            }
        }

        return builder.toString();
    }

    private static int countLeadingSpaces(String line) {
        int index = 0;

        while (index < line.length() && line.charAt(index) == ' ') {
            index++;
        }

        return index;
    }
}
