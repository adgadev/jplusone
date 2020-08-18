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

package com.adgadev.jplusone.asserts.api.builder.exclusion.complex;

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
