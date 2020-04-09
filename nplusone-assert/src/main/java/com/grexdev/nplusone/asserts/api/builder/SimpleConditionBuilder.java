package com.grexdev.nplusone.asserts.api.builder;

import com.grexdev.nplusone.asserts.api.builder.exclusion.simple.SimpleExplicitOperationExclusionsBuilder;
import com.grexdev.nplusone.asserts.api.builder.exclusion.simple.SimpleImplicitOperationExclusionsBuilder;

public interface SimpleConditionBuilder {

    SimpleImplicitOperationExclusionsBuilder implicitOperations();

    SimpleExplicitOperationExclusionsBuilder explicitOperations();

    ConditionDoneBuilder sqlStatementsTotal();

    ConditionDoneBuilder sqlStatementsTotal(SqlStatementType sqlStatementType);

    ConditionDoneBuilder sqlStatementsTotal(SqlStatementGroupType sqlStatementGroupType);

}
