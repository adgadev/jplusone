package com.grexdev.nplusone.core.proxy.datasource;

import com.grexdev.nplusone.core.proxy.StateListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Getter
@RequiredArgsConstructor
public class ProxyContext implements ApplicationListener<ContextRefreshedEvent> {

    private final StateListener stateListener;

    private boolean recording = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.recording = true;
    }

}
