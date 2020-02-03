package com.grexdev.nplusone.core.proxy.datasource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class ParametrizedSql {

    private static final char PARAMETER_SYMBOL = '?';

    private final String sql;

    private final String[] parameters;

    static ParametrizedSql forSql(String sql) {
        return new ParametrizedSql(sql, new String[countParameters(sql)]);
    }

    private static int countParameters(String sql) {
        return (int) sql.chars()
                .filter(character -> character == PARAMETER_SYMBOL)
                .count();
    }

    void clearParameters() {
        Arrays.fill(parameters, null);
    }

    void setInt(int parameterIndex, int value) {
        parameters[parameterIndex - 1] = String.valueOf(value);
    }

    void setLong(int parameterIndex, long value) {
        parameters[parameterIndex - 1] = String.valueOf(value);
    }

    void setString(int parameterIndex, String value) {
        parameters[parameterIndex - 1] = "\"" + value + "\"";
    }

    String getSqlWithParameters() {
        StringBuilder builder = new StringBuilder();
        int parameterIndex = 0;
        int startIndex = 0;
        int endIndex = -1;

        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) == PARAMETER_SYMBOL) {
                endIndex = i;

                if (startIndex < endIndex) {
                    builder.append(sql, startIndex, endIndex);
                }

                String parameterValue = parameters[parameterIndex++];
                builder.append(parameterValue != null ? parameterValue : PARAMETER_SYMBOL);
                startIndex = i + 1;
            }
        }

        if (startIndex < sql.length()) {
            builder.append(sql, startIndex, sql.length());
        }

        return builder.toString();
    }
}
