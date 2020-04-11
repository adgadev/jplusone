package com.grexdev.jplusone.asserts.impl;

import com.grexdev.jplusone.asserts.api.JPlusOneAssertions;
import com.grexdev.jplusone.asserts.api.builder.SessionSelectorBuilder;
import com.grexdev.jplusone.core.registry.RootNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class JPlusOneAssertionsImpl implements JPlusOneAssertions {

    private final RootNode rootNode;

    @Override
    public SessionSelectorBuilder within() {
        return null; // TODO: implement
    }

}
