package com.grexdev.nplusone.core.tracking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static java.lang.Boolean.TRUE;

@Getter
@RequiredArgsConstructor
public class TrackingContext implements ApplicationListener<ContextRefreshedEvent> {

    private final String applicationRootPackage;

    private final boolean debugMode;

    private final ThreadLocal<Boolean> recordingEnabledInCurrentThread = ThreadLocal.withInitial(() -> TRUE);

    private boolean recordingEnabledGlobally = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.recordingEnabledGlobally = true;
    }

    public void enableRecording() {
        recordingEnabledInCurrentThread.set(true);
    }

    public void disableRecording() {
        recordingEnabledInCurrentThread.set(false);
    }

    public boolean isRecordingEnabled() {
        return recordingEnabledGlobally && recordingEnabledInCurrentThread.get();
    }
}
