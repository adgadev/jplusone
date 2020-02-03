package com.grexdev.nplusone.core.tracking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Getter
@RequiredArgsConstructor
public class TrackingContext implements ApplicationListener<ContextRefreshedEvent> {

    private boolean recording = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.recording = true;
    }

}
