package com.grexdev.jplusone.asserts.api.builder;

import com.grexdev.jplusone.asserts.api.JPlusOneAssertionRule;

public interface ConditionDoneBuilder extends JPlusOneAssertionRule {

    ConditionBuilder andShouldBe();

}
