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

package com.adgadev.jplusone.asserts.impl.rule;

import com.adgadev.jplusone.core.frame.FrameExtract;
import com.adgadev.jplusone.core.registry.FrameStack;
import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutionFilter {

    private final List<Class<?>> classes;

    private final String methodName;

    public static ExecutionFilter insideExecutionOfMethod(Class<?> clazz, String methodName) {
        return new ExecutionFilter(Arrays.asList(clazz), methodName);
    }

    public static ExecutionFilter insideExecutionOfAnyMethodIn(Class<?>... classes) {
        return new ExecutionFilter(Arrays.asList(classes), null);
    }

    public static ExecutionFilter insideExecutionOfAnyMethod() {
        return new ExecutionFilter(null, null);
    }

    List<OperationNodeView> getMatchingOperations(SessionNodeView session) {
        boolean sessionStackMatchesExecutionFilter = matchesFrameStack(session.getSessionFrameStack());

        if (sessionStackMatchesExecutionFilter) {
            return session.getOperations().stream()
                    .collect(toList());
        } else {
            return session.getOperations().stream()
                    .filter(operationNode -> matchesFrameStack(operationNode.getCallFramesStack()))
                    .collect(toList());
        }
    }

    private boolean matchesFrameStack(FrameStack frameStack) {
        return frameStack
                .findLastMatchingFrame(this::matchesFrameExtract)
                .isPresent();
    }

    private boolean matchesFrameExtract(FrameExtract frameExtract) {
        return (classes == null || isFrameClassAssignableFromAnyOfClasses(frameExtract.getClazz()))
                && (methodName == null || methodName.equals(frameExtract.getMethodName()));
    }

    private boolean isFrameClassAssignableFromAnyOfClasses(Class<?> frameClass) {
        return classes.stream()
                .anyMatch(clazz -> frameClass.isAssignableFrom(clazz));
    }
}
