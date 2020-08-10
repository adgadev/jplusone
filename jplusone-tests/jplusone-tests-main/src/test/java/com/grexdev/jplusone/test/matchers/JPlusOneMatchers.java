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

package com.grexdev.jplusone.test.matchers;

import com.grexdev.jplusone.test.matchers.frame.FrameCallSequenceMatcher;
import com.grexdev.jplusone.test.matchers.frame.FrameExtractSpecification;
import com.grexdev.jplusone.test.matchers.frame.AllFrameCallMatcher;
import com.grexdev.jplusone.test.matchers.frame.SingleFrameCallMatcher;

import java.util.List;

public class JPlusOneMatchers {

    public static SingleFrameCallMatcher singleFrameCallMatcher(FrameExtractSpecification specification) {
        return new SingleFrameCallMatcher(specification);
    }

    public static FrameCallSequenceMatcher frameCallSequenceMatcher(List<FrameExtractSpecification> specifications) {
        return new FrameCallSequenceMatcher(specifications);
    }

    public static AllFrameCallMatcher allFrameCallMatcher(FrameExtractSpecification specification) {
        return new AllFrameCallMatcher(specification);
    }

}
