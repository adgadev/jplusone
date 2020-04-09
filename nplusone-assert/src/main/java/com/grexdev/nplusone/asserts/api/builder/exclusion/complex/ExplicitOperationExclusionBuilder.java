package com.grexdev.nplusone.asserts.api.builder.exclusion.complex;

public interface ExplicitOperationExclusionBuilder {

    ExplicitExclusionBuilderStage2 fetchingData();

    ExplicitExclusionBuilderStage2 fetchingDataVia(Class<?> clazz, String methodName);

    ExplicitExclusionBuilderStage2 fetchingDataVia(String className, String methodName);

    ExplicitExclusionBuilderStage2 modifyingData();

    ExplicitExclusionBuilderStage2 modifyingDataVia(Class<?> clazz, String methodName);

    ExplicitExclusionBuilderStage2 modifyingDataVia(String className, String methodName);


    interface ExplicitExclusionBuilderStage2 extends ExplicitOperationExclusionBuilder, ExclusionBuilderTimesStage<ExplicitExclusionBuilderStage3> {
    }

    interface ExplicitExclusionBuilderStage3 extends ExplicitOperationExclusionBuilder, ExclusionBuilderSqlStatementsStage<ExplicitExclusionBuilderStage4> {
    }

    interface ExplicitExclusionBuilderStage4 extends ExplicitOperationExclusionBuilder, ExclusionBuilderTimesStage<ExplicitOperationExclusionBuilder> {
    }

}
