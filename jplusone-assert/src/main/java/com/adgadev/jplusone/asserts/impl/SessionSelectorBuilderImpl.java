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

package com.adgadev.jplusone.asserts.impl;

import com.adgadev.jplusone.asserts.api.builder.ExecutionFilterBuilder;
import com.adgadev.jplusone.asserts.api.builder.SessionSelectorBuilder;
import com.adgadev.jplusone.asserts.impl.rule.Rule;
import com.adgadev.jplusone.asserts.impl.rule.SessionSelector;
import com.adgadev.jplusone.core.registry.RootNodeView;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SessionSelectorBuilderImpl implements SessionSelectorBuilder {

    private final Rule rule;

    @Override
    public ExecutionFilterBuilder eachSession() {
        rule.setSessionSelector(RootNodeView::getSessions);
        return new ExecutionFilterBuilderImpl(rule);
    }

    @Override
    public ExecutionFilterBuilder firstSession() {
        rule.setSessionSelector(getSessionSelector(size -> 0));
        return new ExecutionFilterBuilderImpl(rule);
    }

    @Override
    public ExecutionFilterBuilder lastSession() {
        rule.setSessionSelector(getSessionSelector(size -> Math.max(0, size - 1)));
        return new ExecutionFilterBuilderImpl(rule);
    }

    @Override
    public ExecutionFilterBuilder nthSession(int n) {
        rule.setSessionSelector(getSessionSelector(size -> Math.max(0, n)));
        return new ExecutionFilterBuilderImpl(rule);
    }

    private SessionSelector getSessionSelector(Function<Integer, Integer> sizeToElementPosition) {
        return rootNodeView -> rootNodeView.getSessions()
                .stream()
                .skip(sizeToElementPosition.apply(rootNodeView.getSessions().size()))
                .findFirst()
                .map((Function<SessionNodeView, List<SessionNodeView>>) List::of)
                .orElse(emptyList());
    }

}
