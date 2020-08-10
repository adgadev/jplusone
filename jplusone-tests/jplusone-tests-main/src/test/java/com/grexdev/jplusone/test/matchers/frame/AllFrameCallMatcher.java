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

import com.grexdev.jplusone.core.registry.FrameStack;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class AllFrameCallMatcher extends AbstractFrameCallMatcher {

    private final FrameExtractSpecification specification;

    @Override
    protected boolean matchesSafely(FrameStack frameStack) {
        if (frameStack == null || frameStack.getCallFrames() == null) {
            return false;
        }

        return frameStack.getCallFrames().stream()
                .allMatch(specification::isSatisfiedBy);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("All frames matching following criteria: ");
        description.appendValue(specification.getDescription());
    }
}