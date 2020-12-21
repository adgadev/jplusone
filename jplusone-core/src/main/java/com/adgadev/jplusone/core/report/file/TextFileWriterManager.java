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

package com.adgadev.jplusone.core.report.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TextFileWriterManager {

    private static TextFileWriterManager INSTANCE = null;

    private final Map<File, TextFileWriterImpl> textFileWriterByFile = new HashMap<>();

    public synchronized static TextFileWriterManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TextFileWriterManager();
        }

        return INSTANCE;
    }

    private TextFileWriterManager() {
        log.debug("Initializing JPlusOne file report manager");
        Thread shutdownHook = new Thread(this::close);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public synchronized TextFileWriter resolveFileWriter(String filePath) {
        File outputFile = Paths.get(filePath).toFile();
        log.info("Obtaining logger for file: {}", outputFile.getAbsolutePath());

        if (textFileWriterByFile.containsKey(outputFile)) {
            return textFileWriterByFile.get(outputFile);

        } else {
            TextFileWriterImpl textFileWriter = new TextFileWriterImpl(outputFile);
            textFileWriterByFile.put(outputFile, textFileWriter);
            return textFileWriter;
        }
    }

    private void close() {
        textFileWriterByFile.values().forEach(TextFileWriterImpl::close);
        log.debug("JPlusOne file report manager closed");
    }
}
