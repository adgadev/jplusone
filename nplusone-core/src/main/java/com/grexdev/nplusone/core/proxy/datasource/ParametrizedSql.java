package com.grexdev.nplusone.core.proxy.datasource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class ParametrizedSql {

    private final String sql;

    private final Map<Integer, String> parameters; // TODO: optimize with with array since amount of parameters can be resolved from 'sql' value

    static ParametrizedSql forSql(String sql) {
        return new ParametrizedSql(sql, new HashMap<>());
    }

    void clearParameters() {
        parameters.clear();
    }

    void setInt(int parameterIndex, int value) {
        parameters.put(parameterIndex, String.valueOf(value));
    }

    void setLong(int parameterIndex, long value) {
        parameters.put(parameterIndex, String.valueOf(value));
    }

    void setString(int parameterIndex, String value) {
        parameters.put(parameterIndex, "\"" + value + "\"");
    }

    String getSqlWithParameters() {
        StringBuilder builder = new StringBuilder();
        int parameterIndex = 1;
        int startIndex = 0;
        int endIndex = -1;

        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) == '?') {
                endIndex = i;

                if (startIndex < endIndex) {
                    builder.append(sql, startIndex, endIndex);
                }

                builder.append(parameters.getOrDefault(parameterIndex++, "?"));
                startIndex = i + 1;
            }
        }

        if (startIndex < sql.length()) {
            builder.append(sql, startIndex, sql.length());
        }

        return builder.toString();
    }
}
