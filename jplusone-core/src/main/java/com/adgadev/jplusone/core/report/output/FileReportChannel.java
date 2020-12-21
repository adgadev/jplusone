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

package com.adgadev.jplusone.core.report.output;


import com.adgadev.jplusone.core.report.file.TextFileWriterManager;
import com.adgadev.jplusone.core.report.file.TextFileWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

class FileReportChannel implements ReportChannel {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final AtomicInteger sessionCounter = new AtomicInteger(0);

    private final TextFileWriter textFileWriter;

    private final int springContextNumber;

    public FileReportChannel(String fileName, int springContextNumber) {
        this.springContextNumber = springContextNumber;
        this.textFileWriter = TextFileWriterManager.getInstance()
                .resolveFileWriter(fileName);
    }

    @Override
    public void printMessage(MessageType messageType, String message) {
        if (messageType == MessageType.REPORT) {
            String formattedDateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            String header = String.format("\n\n================================================ [Context %d, Session %d, %s] ============================================",
                    springContextNumber, sessionCounter.incrementAndGet(), formattedDateTime);

            textFileWriter.write(header);
            textFileWriter.write(message);
        }
    }
}
