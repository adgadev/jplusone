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

package com.adgadev.jplusone.asserts.api.builder;

import java.util.Arrays;
import java.util.function.Function;

public interface AmountMatcher extends Function<Integer, Boolean> {

    static AmountMatcher none() {
        return amount -> amount == 0;
    }

    static AmountMatcher exactly(int value) {
        return amount -> amount == value;
    }

    static AmountMatcher atMost(int value) {
        return amount -> amount <= value;
    }

    static AmountMatcher atLeast(int value) {
        return amount -> amount >= value;
    }

    static AmountMatcher moreThan(int value) {
        return amount -> amount > value;
    }

    static AmountMatcher lessThan(int value) {
        return amount -> amount < value;
    }

    static AmountMatcher anyNumber() {
        return amount -> true;
    }

    static AmountMatcher range(int startInclusive, int endInclusive) {
        // TODO: add assertions for arguments in all methods here
        return amount -> amount >= startInclusive && amount <= endInclusive;
    }

    static AmountMatcher oneOf(int... values) {
        return amount -> Arrays.asList(values).contains(amount);
    }
}
