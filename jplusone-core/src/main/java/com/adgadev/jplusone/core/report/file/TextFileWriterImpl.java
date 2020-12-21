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

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
class TextFileWriterImpl implements TextFileWriter, Closeable {

    private static final int BUFFER_SIZE = 8192;

    private Writer writer;

    TextFileWriterImpl(File outputFile) {
        try {
            preparePathStructure(outputFile);
            OutputStreamWriter rawWriter = new OutputStreamWriter(new FileOutputStream(outputFile), UTF_8);
            writer = new BufferedWriter(rawWriter, BUFFER_SIZE);

        } catch (Exception e) {
            log.error("Failed to create JPlusOne report output file \"" + outputFile + "\"", e);
            writer = null;
        }
    }

    private void preparePathStructure(File outputFile) throws IOException {
        if (outputFile.exists()) {
            ensureThat(outputFile.isFile(), "File path \"" + outputFile + "\" does not point to a regular file");
            ensureThat(outputFile.canWrite(), "Insufficient permissions to write to a file \"" + outputFile + "\"");
        } else {
            Path outputFileDirectoryPath = Paths.get(outputFile.getParentFile().getCanonicalPath());
            Files.createDirectories(outputFileDirectoryPath);
        }
    }

    @Override
    public synchronized void write(String data) {
        if (writer != null) {
            try {
                writer.append(data);
                writer.flush();
            } catch (IOException e) {
                log.warn("Fail to append data", e);
            }
        }
    }

    @Override
    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                log.warn("Error during closing JPlusOne report file publisher", e);
            }
        }
    }

    private void ensureThat(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
