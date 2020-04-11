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

package com.grexdev.jplusone.core.report;

import com.github.vertical_blank.sqlformatter.SqlFormatter;

class ReportSqlFormatter {

    private static final String SQL_FORMATTER_INTEND = " ";

    private static final String SELECT_CLAUSE = "select [...] from";

    // TODO: refactor
    static String formatSql(String generalIntend, String sql) {
        boolean selectStatement = sql.startsWith(SELECT_CLAUSE);
        String formattedSql = SqlFormatter.format(sql, SQL_FORMATTER_INTEND);
        String[] lines = formattedSql.split("\n");
        StringBuilder builder = new StringBuilder();
        int startIndex = 0;

        if (selectStatement) {
            builder.append('\n');
            builder.append(generalIntend);
            builder.append(SELECT_CLAUSE);
            startIndex = 3;
        }

        for (int i = startIndex; i < lines.length; i++) {
            String line = lines[i];
            builder.append('\n');
            builder.append(generalIntend);
            builder.append(line.startsWith(SQL_FORMATTER_INTEND) ? "\t" : "");
            builder.append(line);
        }

        return builder.toString();
    }

}
