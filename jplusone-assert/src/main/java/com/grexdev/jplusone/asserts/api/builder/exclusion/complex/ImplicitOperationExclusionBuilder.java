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
