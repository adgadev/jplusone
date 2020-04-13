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
        parameters[parameterIndex - 1] = "'" + value + "'";
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
