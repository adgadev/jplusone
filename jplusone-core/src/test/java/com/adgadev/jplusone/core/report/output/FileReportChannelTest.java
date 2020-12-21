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

import com.adgadev.jplusone.core.report.output.ReportChannel.MessageType;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.CombinableMatcher.both;

class FileReportChannelTest {

    private FileSystem fileSystem;

    @BeforeEach
    public void setUp() {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
    }

    @AfterEach
    public void tearDown() throws IOException {
        fileSystem.close();
    }

    @Test
    public void shouldPrintReportMessage() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("jplusone-report.txt");
        ReportChannel channel = new FileReportChannel(reportFilePath, 3);
        String content = "\nFirst line\nSecond line\n";

        // when
        channel.printMessage(MessageType.REPORT, content);

        // then
        assertThat(getTextFileContent(reportFilePath), both(endsWith(content))
                .and(startsWith("\n\n================================================ [Context 3, Session 1"))
                .and(matchesRegex("\n\n.*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}.*\n.*.*\n.*\n"))
        );
    }

    @Test
    public void shouldPrintTextMessage() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("jplusone-report.txt");
        ReportChannel channel = new FileReportChannel(reportFilePath, 3);

        // when
        channel.printMessage(MessageType.TEXT, "Simple text message");

        // then
        assertThat(getTextFileContent(reportFilePath), is(emptyString()));
    }

    private String getTextFileContent(Path reportFilePath) throws IOException {
        return new String(Files.readAllBytes(reportFilePath), StandardCharsets.UTF_8);
    }
}