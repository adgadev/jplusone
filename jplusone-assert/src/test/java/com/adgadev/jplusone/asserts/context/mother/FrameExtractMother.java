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

package com.adgadev.jplusone.asserts.context.mother;

import com.adgadev.jplusone.core.frame.FrameClassKind;
import com.adgadev.jplusone.core.frame.FrameExtract;

import java.util.Random;

public class FrameExtractMother {

    public static FrameExtract anyFrameExtract() {
        return anyFrameExtractA();
    }

    public static FrameExtract anyFrameExtractA() {
        return anyFrameExtract(SampleFrameClassA.class, "methodA");
    }

    public static FrameExtract anyFrameExtractB() {
        return anyFrameExtract(SampleFrameClassB.class, "methodB");
    }

    public static FrameExtract anyFrameExtract(Class<?> clazz, String methodName) {
        return new FrameExtract(
                FrameClassKind.APPLICATION_CLASS,
                clazz,
                clazz.getCanonicalName(),
                methodName,
                clazz.getSimpleName() + ".java",
                new Random().nextInt(500) + 1
        );
    }

    public static class SampleFrameClassA {
    }

    public static class SampleFrameClassB {
    }

    public static class SampleFrameClassC {
    }

    public static class SampleFrameClassD {
    }

    public static class SampleFrameClassE {
    }
}
