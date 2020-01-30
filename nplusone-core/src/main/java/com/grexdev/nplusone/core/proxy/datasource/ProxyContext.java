package com.grexdev.nplusone.core.proxy.datasource;

import lombok.Getter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Getter
public class ProxyContext implements ApplicationListener<ContextRefreshedEvent> {

    private boolean recording = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.recording = true;
    }
}
