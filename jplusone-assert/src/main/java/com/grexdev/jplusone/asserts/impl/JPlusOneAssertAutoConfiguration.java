package com.grexdev.jplusone.asserts.impl;

import com.grexdev.jplusone.asserts.api.JPlusOneAssertions;
import com.grexdev.jplusone.core.JPlusOneAutoConfiguration;
import com.grexdev.jplusone.core.registry.RootNode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(RootNode.class)
@AutoConfigureAfter(JPlusOneAutoConfiguration.class)
class JPlusOneAssertAutoConfiguration {

    private final RootNode rootNode;

    @Bean
    public JPlusOneAssertions jPlusOneAssertions() {
        return new JPlusOneAssertionsImpl(rootNode);
    }
}
