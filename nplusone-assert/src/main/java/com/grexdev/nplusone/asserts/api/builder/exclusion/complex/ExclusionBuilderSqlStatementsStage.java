package com.grexdev.nplusone.asserts.api.builder.exclusion.complex;

import com.grexdev.nplusone.asserts.api.builder.SqlStatementGroupType;
import com.grexdev.nplusone.asserts.api.builder.SqlStatementType;

public interface ExclusionBuilderSqlStatementsStage<T> {

    T usingSqlStatements();

    T usingSqlStatements(SqlStatementType sqlStatementType);

    T usingSqlStatements(SqlStatementGroupType sqlStatementGroupType);

}
