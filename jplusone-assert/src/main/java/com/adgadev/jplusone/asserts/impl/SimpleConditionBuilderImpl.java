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

package com.adgadev.jplusone.asserts.impl;

import com.adgadev.jplusone.asserts.api.builder.ConditionDoneBuilder;
import com.adgadev.jplusone.asserts.api.builder.SimpleConditionBuilder;
import com.adgadev.jplusone.asserts.api.builder.SqlStatementGroupType;
import com.adgadev.jplusone.asserts.api.builder.SqlStatementType;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.SimpleExplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.SimpleImplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.impl.rule.Rule;
import com.adgadev.jplusone.asserts.impl.rule.Condition;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.StatementType;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SimpleConditionBuilderImpl implements SimpleConditionBuilder {

    private static Map<SqlStatementType, StatementType> SQL_STATEMENT_TYPE_TO_IMPL = Map.of(
            SqlStatementType.SELECT_STATEMENT, StatementType.SELECT,
            SqlStatementType.INSERT_STATEMENT, StatementType.INSERT,
            SqlStatementType.UPDATE_STATEMENT, StatementType.UPDATE,
            SqlStatementType.DELETE_STATEMENT, StatementType.DELETE,
            SqlStatementType.OTHER_STATEMENT, StatementType.OTHER
    );

    private static Map<SqlStatementGroupType, Set<StatementType>> SQL_STATEMENT_GROUP_TYPE_TO_IMPL = Map.of(
            SqlStatementGroupType.READ_STATEMENTS, EnumSet.of(StatementType.SELECT),
            SqlStatementGroupType.WRITE_STATEMENTS, EnumSet.of(StatementType.INSERT, StatementType.UPDATE, StatementType.DELETE),
            SqlStatementGroupType.OTHER_STATEMENTS, EnumSet.of(StatementType.OTHER)
    );

    private final Rule rule;

    private final Condition condition;

    @Override
    public SimpleImplicitOperationExclusionsBuilder implicitOperations() {
        condition.setOperationType(OperationType.IMPLICIT);
        return new ExtendedImplicitOperationExclusionBuilderImpl(rule, condition);
    }

    @Override
    public SimpleExplicitOperationExclusionsBuilder explicitOperations() {
        condition.setOperationType(OperationType.EXPLICIT);
        return new ExtendedExplicitOperationExclusionBuilderImpl(rule, condition);
    }

    @Override
    public ConditionDoneBuilder sqlStatementsTotal() {
        condition.setStatementTypes(new HashSet<>(asList(StatementType.values())));
        return new ConditionDoneBuilderImpl(rule);
    }

    @Override
    public ConditionDoneBuilder sqlStatementsTotal(@NonNull SqlStatementType sqlStatementType) {
        StatementType statementType = SQL_STATEMENT_TYPE_TO_IMPL.get(sqlStatementType);
        condition.setStatementTypes(Set.of(statementType));
        return new ConditionDoneBuilderImpl(rule);
    }

    @Override
    public ConditionDoneBuilder sqlStatementsTotal(@NonNull SqlStatementGroupType sqlStatementGroupType) {
        Set<StatementType> statementTypes = SQL_STATEMENT_GROUP_TYPE_TO_IMPL.get(sqlStatementGroupType);
        condition.setStatementTypes(statementTypes);
        return new ConditionDoneBuilderImpl(rule);
    }

}
