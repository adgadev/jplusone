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

package com.grexdev.jplusone.test.matchers.frame;

import com.grexdev.jplusone.core.frame.FrameExtract;
import com.grexdev.jplusone.test.matchers.common.AndCondition;
import com.grexdev.jplusone.test.matchers.common.SimpleCondition;
import com.grexdev.jplusone.test.matchers.common.Specification;

import java.util.List;

import static com.grexdev.jplusone.core.frame.FrameClassKind.*;

public class FrameExtractSpecification extends AndCondition<FrameExtract> {

    private FrameExtractSpecification(List<Specification<FrameExtract>> conditions) {
        super(conditions);
    }

    public static FrameExtractSpecification anyAppMethodCallFrame(Class<?> clazz, String methodName) {
        return new FrameExtractSpecification(List.of(
                SimpleCondition.of(FrameExtract::getType, APPLICATION_CLASS::equals, String.format("type = %s", APPLICATION_CLASS)),
                SimpleCondition.of(FrameExtract::getClazz, clazz::equals, String.format("class = %s", clazz.getSimpleName())),
                SimpleCondition.of(FrameExtract::getMethodName, methodName::equals, String.format("methodName = %s", methodName))
        ));
    }

    public static FrameExtractSpecification anyProxyMethodCallFrame(Class<?> clazz, String methodName) {
        return new FrameExtractSpecification(List.of(
                SimpleCondition.of(FrameExtract::getType, type -> type == APPLICATION_INTERFACE || type == APPLICATION_SUPERCLASS, String.format("type = PROXY")),
                SimpleCondition.of(FrameExtract::getClazz, clazz::equals, String.format("class = %s", clazz.getSimpleName())),
                SimpleCondition.of(FrameExtract::getMethodName, methodName::equals, String.format("methodName = %s", methodName))
        ));
    }

    public static FrameExtractSpecification anyThirdPartyMethodCallFrame(Class<?> clazz, String methodName) {
        return new FrameExtractSpecification(List.of(
                SimpleCondition.of(FrameExtract::getType, THIRD_PARTY_CLASS::equals, String.format("type = %s", THIRD_PARTY_CLASS)),
                SimpleCondition.of(FrameExtract::getClazz, clazz::equals, String.format("class = %s", clazz.getSimpleName())),
                SimpleCondition.of(FrameExtract::getMethodName, methodName::equals, String.format("methodName = %s", methodName))
        ));
    }
}
