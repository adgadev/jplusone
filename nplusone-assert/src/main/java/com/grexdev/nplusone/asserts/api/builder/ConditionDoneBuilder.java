package com.grexdev.nplusone.asserts.api.builder;

import com.grexdev.nplusone.asserts.api.NPlusOneAssertionRule;

public interface ConditionDoneBuilder extends NPlusOneAssertionRule {

    ConditionBuilder andShouldBe();

}
