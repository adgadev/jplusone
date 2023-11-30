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

class TextFileWriterImplTest {

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
    public void shouldCreexitateNewEmptyFile() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("virtual/reports/jplusone-report.txt");

        // when
        new TextFileWriterImpl(reportFilePath);

        // then
        assertThat(getTextFileContent(reportFilePath), is(emptyString()));
    }

    @Test
    public void shouldTruncateExistingFile() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("virtual/reports/jplusone-report.txt");
        Files.createDirectories(fileSystem.getPath("virtual/reports"));
        Files.write(reportFilePath, "Old Data".getBytes());

        // when
        new TextFileWriterImpl(reportFilePath);

        // then
        assertThat(getTextFileContent(reportFilePath), is(emptyString()));
    }

    @Test
    public void shouldWriteTextToFile() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("virtual/reports/jplusone-report.txt");
        TextFileWriterImpl textFileWriter = new TextFileWriterImpl(reportFilePath);

        // when
        textFileWriter.write("Hello File!\n");

        // then
        assertThat(getTextFileContent(reportFilePath), equalTo("Hello File!\n"));
    }

    @Test
    public void shouldWriteTextToFileMultipleTimes() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("virtual/reports/jplusone-report.txt");
        TextFileWriterImpl textFileWriter = new TextFileWriterImpl(reportFilePath);
        textFileWriter.write("Hello File!\n");

        // when
        textFileWriter.write("... and hello again!\n");

        // then
        assertThat(getTextFileContent(reportFilePath), equalTo("Hello File!\n... and hello again!\n"));
    }

    @Test
    public void shouldWriteAfterCloseBeIgnored() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("virtual/reports/jplusone-report.txt");
        TextFileWriterImpl textFileWriter = new TextFileWriterImpl(reportFilePath);
        textFileWriter.write("Hello File!\n");

        // when
        textFileWriter.close();

        // then
        textFileWriter.write("... and hello again!\n");
        assertThat(getTextFileContent(reportFilePath), equalTo("Hello File!\n"));
    }

    @Test
    public void shouldDoNothingWhenFilePathPointsToExistingDirectory() throws IOException {
        // given
        Path reportFilePath = fileSystem.getPath("virtual/reports");
        Files.createDirectories(fileSystem.getPath("virtual/reports"));

        // when
        new TextFileWriterImpl(reportFilePath);

        // then
        assertThat(Files.isRegularFile(reportFilePath), equalTo(false));
    }

    private String getTextFileContent(Path reportFilePath) throws IOException {
        return new String(Files.readAllBytes(reportFilePath), StandardCharsets.UTF_8);
    }
}