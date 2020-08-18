/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adgadev.jplusone.core.tracking;

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

    private final VerbosityLevel verbosityLevel;

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
