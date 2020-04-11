package com.grexdev.jplusone.asserts.api.builder.exclusion.complex;

public interface ImplicitOperationExclusionBuilder {

    ImplicitExclusionBuilderStage2 loadingEntity(Class<?> entityClazz);

    ImplicitExclusionBuilderStage2 loadingAnyEntity();

    ImplicitExclusionBuilderStage2 loadingCollection(Class<?> entityClass, String associationField);

    ImplicitExclusionBuilderStage2 loadingAnyCollectionInEntity(Class<?> entityClass);

    ImplicitExclusionBuilderStage2 loadingAnyCollection();

    ImplicitExclusionBuilderStage2 flushingData();


    interface ImplicitExclusionBuilderStage2 extends ImplicitOperationExclusionBuilder, ExclusionBuilderTimesStage<ImplicitExclusionBuilderStage3> {
    }

    interface ImplicitExclusionBuilderStage3 extends ImplicitOperationExclusionBuilder, ExclusionBuilderSqlStatementsStage<ImplicitExclusionBuilderStage4> {
    }

    interface ImplicitExclusionBuilderStage4 extends ImplicitOperationExclusionBuilder, ExclusionBuilderTimesStage<ImplicitOperationExclusionBuilder> {
    }
}
