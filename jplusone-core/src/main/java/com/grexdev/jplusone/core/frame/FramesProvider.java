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

import com.grexdev.jplusone.core.utils.StreamUtils;
import com.grexdev.jplusone.core.registry.FrameStack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.lang.StackWalker.Option.SHOW_HIDDEN_FRAMES;
import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
public class FramesProvider {

    private static final Set<Option> WALKER_OPTIONS = EnumSet.of(Option.RETAIN_CLASS_REFERENCE, SHOW_HIDDEN_FRAMES);

    private static final String[] PROXY_CLASS_NAME_MARKERS = { "$Proxy", "$HibernateProxy", "$$EnhancerBySpringCGLIB", "$$FastClassBySpringCGLIB" };

    private static final Consumer<FrameExtract> LOG_FRAME_EXTRACT_CLOSURE = frame -> log.info(frame.toString());

    private static final Consumer<FrameExtract> NOOP_CLOSURE = frame -> {};

    private final FrameClassFactory frameClassFactory;

    private final Consumer<FrameExtract> frameProcessingAdditionalAction;

    public FramesProvider(String applicationRootPackage, boolean debugMode) {
        this.frameClassFactory = new FrameClassFactory(applicationRootPackage, PROXY_CLASS_NAME_MARKERS);
        this.frameProcessingAdditionalAction = debugMode ? LOG_FRAME_EXTRACT_CLOSURE : NOOP_CLOSURE;
    }

    public FrameStack captureCallFrames() {
        List<FrameExtract> callFrames = StackWalker.getInstance(WALKER_OPTIONS)
                .walk(this::collectFrames);

        return new FrameStack(callFrames);
    }

    private List<FrameExtract> collectFrames(Stream<StackFrame> stream) {
        return stream
                .filter(stackFrame -> nonNull(stackFrame.getDeclaringClass().getCanonicalName()))
                .map(frameClassFactory::createFrameClass)
                .map(FrameExtract::new)
                .peek(frameProcessingAdditionalAction)
                .collect(StreamUtils.toListReversed());
    }

}
