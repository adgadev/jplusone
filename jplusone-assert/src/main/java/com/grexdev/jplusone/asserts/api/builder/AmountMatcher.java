package com.grexdev.jplusone.asserts.api.builder;

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

}
