package com.grexdev.jplusone.asserts.api.builder;

import com.grexdev.jplusone.asserts.api.builder.exclusion.simple.SimpleImplicitOperationExclusionsBuilder;
import com.grexdev.jplusone.asserts.api.builder.exclusion.simple.SimpleExplicitOperationExclusionsBuilder;

public interface SimpleConditionBuilder {

    SimpleImplicitOperationExclusionsBuilder implicitOperations();

    SimpleExplicitOperationExclusionsBuilder explicitOperations();

    ConditionDoneBuilder sqlStatementsTotal();

    ConditionDoneBuilder sqlStatementsTotal(SqlStatementType sqlStatementType);

    ConditionDoneBuilder sqlStatementsTotal(SqlStatementGroupType sqlStatementGroupType);

}
