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

import com.adgadev.jplusone.core.frame.FrameExtract;
import com.adgadev.jplusone.core.registry.FrameStack;

import java.util.List;

import static com.adgadev.jplusone.asserts.context.mother.FrameExtractMother.*;
import static java.util.Arrays.asList;

public class FrameStackMother {

    public static FrameStack anyFrameStack() {
        return new FrameStack(asList(anyFrameExtract()));
    }

    public static FrameStack anyFrameStackForOperation() {
        return new FrameStack(asList(anyFrameExtractA()));
    }

    public static FrameStack anyFrameStackForSession() {
        return new FrameStack(asList(anyFrameExtractB()));
    }

    public static FrameStack anyFrameStack(FrameExtract frameExtract) {
        return new FrameStack(asList(frameExtract));
    }

    public static FrameStack anyFrameStack(List<FrameExtract> frameExtracts) {
        return new FrameStack(frameExtracts);
    }
}
