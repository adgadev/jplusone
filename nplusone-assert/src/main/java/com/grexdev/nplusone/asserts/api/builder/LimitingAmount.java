package com.grexdev.nplusone.asserts.api.builder;

public interface LimitingAmount<T> {

    T none();

    T exactly(int amount);

    T atMost(int amount);

    T atLeast(int amount);

    T moreThan(int amount);

    T lessThan(int amount);

}
