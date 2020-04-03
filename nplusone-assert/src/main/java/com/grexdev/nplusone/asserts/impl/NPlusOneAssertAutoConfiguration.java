package com.grexdev.nplusone.asserts.impl;

import com.grexdev.nplusone.asserts.api.NPlusOneAssertions;
import com.grexdev.nplusone.core.NPlusOneAutoConfiguration;
import com.grexdev.nplusone.core.registry.RootNode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(RootNode.class)
@AutoConfigureAfter(NPlusOneAutoConfiguration.class)
class NPlusOneAssertAutoConfiguration {

    private final RootNode rootNode;

    @Bean
    public NPlusOneAssertions nPlusOneAssertions() {
        return new NPlusOneAssertionsImpl(rootNode);
    }
}
