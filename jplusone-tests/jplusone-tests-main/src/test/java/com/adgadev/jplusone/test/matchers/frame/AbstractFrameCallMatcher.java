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

package com.adgadev.jplusone.test.matchers.frame;

import com.adgadev.jplusone.core.frame.FrameExtract;
import com.adgadev.jplusone.core.registry.FrameStack;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public abstract class AbstractFrameCallMatcher extends TypeSafeMatcher<FrameStack> {

    @Override
    protected void describeMismatchSafely(FrameStack frameStack, Description mismatchDescription) {
        mismatchDescription
                .appendText("not found in frame stack:" + frameStackToString(frameStack));
    }

    protected String frameStackToString(FrameStack frameStack) {
        StringBuilder builder = new StringBuilder();

        for (FrameExtract frame : frameStack.getCallFrames()) {
            builder.append("\n        ");
            builder.append(frame.toString());
        }

        return builder.toString();
    }
}
