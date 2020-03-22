package com.grexdev.nplusone.core.report;

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
