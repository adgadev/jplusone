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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public class FrameCallSequenceMatcher extends AbstractFrameCallMatcher {

    private final List<FrameExtractSpecification> specifications;

    @Override
    protected boolean matchesSafely(FrameStack frameStack) {
        return match(frameStack).matched;
    }

    @Override
    public void describeTo(Description description) {
        String specificationsDescription = specifications.stream()
                .map(FrameExtractSpecification::getDescription)
                .collect(joining(",\n\t"));
        description.appendText("Frames matching sequence of criteria: [\n\t" + specificationsDescription + "\n]");
    }

    @Override
    protected void describeMismatchSafely(FrameStack frameStack, Description mismatchDescription) {
        String errorDescription = Optional.ofNullable(match(frameStack).failedOnSpecification)
                .map(FrameExtractSpecification::getDescription)
                .orElse("");
        mismatchDescription.appendText("[" + errorDescription + "] not found in frame stack: " + frameStackToString(frameStack));
    }

    private MatchingResult match(FrameStack frameStack) {
        if (frameStack == null || frameStack.getCallFrames() == null) {
            return MatchingResult.of(false, null);
        }

        Iterator<FrameExtractSpecification> frameSpecificationIterator = specifications.iterator();
        Iterator<FrameExtract> frameIterator = frameStack.getCallFrames().iterator();

        while (frameSpecificationIterator.hasNext()) {
            FrameExtractSpecification frameSpecification = frameSpecificationIterator.next();
            boolean matchingFrameFound = false;

            while (!matchingFrameFound && frameIterator.hasNext()) {
                if (frameSpecification.isSatisfiedBy(frameIterator.next())) {
                    matchingFrameFound = true;
                }
            }

            if (!matchingFrameFound) {
                return MatchingResult.of(false, frameSpecification);
            }
        }

        return MatchingResult.of(true, null);
    }

    @RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    private static class MatchingResult {

        private final boolean matched;

        private final FrameExtractSpecification failedOnSpecification;

    }
}