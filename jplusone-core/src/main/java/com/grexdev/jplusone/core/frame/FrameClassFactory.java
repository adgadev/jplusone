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

package com.grexdev.jplusone.core.frame;

import lombok.extern.slf4j.Slf4j;

import java.lang.StackWalker.StackFrame;
import java.util.Arrays;

import static com.grexdev.jplusone.core.frame.FrameClassKind.*;

@Slf4j
class FrameClassFactory {

    private static final String PACKAGE_SEPARATOR = ".";

    private final String applicationRootPackage;

    private final String[] proxyClassNameMarkers;

    public FrameClassFactory(String applicationRootPackage, String[] proxyClassNameMarkers) {
        log.debug("Using package '{}' as a root application package", applicationRootPackage);
        this.applicationRootPackage = applicationRootPackage.endsWith(PACKAGE_SEPARATOR)
                ? applicationRootPackage : applicationRootPackage + PACKAGE_SEPARATOR;
        this.proxyClassNameMarkers = proxyClassNameMarkers;
    }

    FrameClass createFrameClass(StackFrame stackFrame) {
        Class<?> frameClass = stackFrame.getDeclaringClass();

        if (isClassInsidePackage(frameClass) && isClassNotAProxy(frameClass)) {
            return FrameClass.of(APPLICATION_CLASS, frameClass, stackFrame);

        } else if (frameClass.getSuperclass() != null && isClassInsidePackage(frameClass.getSuperclass())) {
            return FrameClass.of(APPLICATION_SUPERCLASS, frameClass.getSuperclass(), stackFrame);

        } else {
            return Arrays.stream(frameClass.getInterfaces())
                    .filter(this::isClassInsidePackage)
                    .filter(interfaceClass -> classContainsMethod(interfaceClass, stackFrame.getMethodName()))
                    .findFirst()
                    .map(clazz -> FrameClass.of(APPLICATION_INTERFACE, clazz, stackFrame))
                    .orElse(FrameClass.of(THIRD_PARTY_CLASS, frameClass, stackFrame));
        }
    }

    private boolean isClassInsidePackage(Class<?> clazz) {
        return clazz.getName().startsWith(applicationRootPackage);
    }

    private boolean classContainsMethod(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.getName().equals(methodName));
    }

    private boolean isClassNotAProxy(Class<?> clazz) {
        return !Arrays.stream(proxyClassNameMarkers)
                .anyMatch(clazz.getCanonicalName()::contains);
    }
}
