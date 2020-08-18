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

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatementType {
    SELECT("select", StatementGroupType.READ),
    INSERT("insert", StatementGroupType.WRITE),
    UPDATE("update", StatementGroupType.WRITE),
    DELETE("delete", StatementGroupType.WRITE),
    OTHER(null, StatementGroupType.OTHER);

    public enum StatementGroupType {READ, WRITE, OTHER}

    private static final int MAX_CLAUSE_SIZE = 6;

    private final String clause;

    @Getter
    private final StatementGroupType statementGroupType;

    static StatementType resolveStatementType(String sql) {
        int endIndex = Math.min(MAX_CLAUSE_SIZE, sql.length());
        String sqlPrefix = sql.substring(0, endIndex);

        return Arrays.stream(values())
                .filter(statementType -> statementType != OTHER)
                .filter(statementType -> sqlPrefix.startsWith(statementType.clause))
                .findFirst()
                .orElse(StatementType.OTHER);
    }

}
