package com.grexdev.nplusone.asserts.api.builder.exclusion.complex;

import com.grexdev.nplusone.asserts.api.builder.AmountMatcher;

public interface ExclusionBuilderTimesStage<T> {

    T times(int amount);

    T times(AmountMatcher amountMatcher);

}
