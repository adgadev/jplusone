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

package com.adgadev.jplusone.asserts.impl.rule.exclusion;

import com.adgadev.jplusone.core.frame.FrameExtract;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Objects;

import static com.adgadev.jplusone.asserts.impl.rule.exclusion.ValueHolder.of;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FetchingDataCriteria {

    private final ValueHolder<Class<?>> clazz;

    private final ValueHolder<String> methodName;

    public static FetchingDataCriteria fetchingDataCriteria() {
        return new FetchingDataCriteria(null, null);
    }

    public static FetchingDataCriteria fetchingDataViaAnyMethodCriteria(Class<?> clazz) {
        return new FetchingDataCriteria(of(clazz), null);
    }

    public static FetchingDataCriteria fetchingDataViaCriteria(Class<?> clazz, String methodName) {
        return new FetchingDataCriteria(of(clazz), of(methodName));
    }

    @SneakyThrows
    public static FetchingDataCriteria fetchingDataViaCriteria(String className, String methodName) {
        return new FetchingDataCriteria(of(Class.forName(className)), of(methodName));
    }

    public boolean matches(FrameExtract frameExtract) {
        return (clazz == null || clazz.getValue().isAssignableFrom(frameExtract.getClazz()))
                && (methodName == null || Objects.equals(methodName.getValue(), frameExtract.getMethodName()));
    }
}
