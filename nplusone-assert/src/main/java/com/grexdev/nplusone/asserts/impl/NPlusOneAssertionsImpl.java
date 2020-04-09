package com.grexdev.nplusone.asserts.impl;

import com.grexdev.nplusone.asserts.api.NPlusOneAssertions;
import com.grexdev.nplusone.asserts.api.builder.SessionSelectorBuilder;
import com.grexdev.nplusone.core.registry.RootNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class NPlusOneAssertionsImpl implements NPlusOneAssertions {

    private final RootNode rootNode;

    @Override
    public SessionSelectorBuilder within() {
        return null; // TODO: implement
    }

}
