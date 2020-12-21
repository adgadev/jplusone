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
import java.nio.file.FileSystem;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;

class TextFileWriterManagerTest {

    private FileSystem fileSystem;

    @BeforeEach
    public void setUp() {
        fileSystem = Jimfs.newFileSystem(Configuration.unix()
                .toBuilder()
                .setWorkingDirectory("/home/user")
                .build());
    }

    @AfterEach
    public void tearDown() throws IOException {
        fileSystem.close();
    }

    @Test
    public void shouldCreateTextFileWriter() {
        Path outputFilePath = fileSystem.getPath("jplusone-report1.txt");
        TextFileWriter textFileWriter = TextFileWriterManager.getInstance().resolveFileWriter(outputFilePath);

        assertThat(textFileWriter, is(notNullValue()));
    }

    @Test
    public void shouldUseCachedTextFileWriter() {
        Path outputFilePath = fileSystem.getPath("jplusone-report2.txt");
        TextFileWriter firstTextFileWriter = TextFileWriterManager.getInstance().resolveFileWriter(outputFilePath);
        TextFileWriter secondTextFileWriter = TextFileWriterManager.getInstance().resolveFileWriter(outputFilePath);

        assertThat(firstTextFileWriter, is(secondTextFileWriter));
    }

    @Test
    public void shouldUseCachedTextFileWriterForEqualPaths() {
        Path firstOutputFilePath = fileSystem.getPath("../user/jplusone-report3.txt");
        Path secondOutputFilePath = fileSystem.getPath("/home/user/jplusone-report3.txt");

        TextFileWriter firstTextFileWriter = TextFileWriterManager.getInstance().resolveFileWriter(firstOutputFilePath);
        TextFileWriter secondTextFileWriter = TextFileWriterManager.getInstance().resolveFileWriter(secondOutputFilePath);

        assertThat(firstTextFileWriter, is(secondTextFileWriter));
    }

    @Test
    public void shouldNotUseCachedTextFileWriterForNonEqualPaths() {
        Path firstOutputFilePath = fileSystem.getPath("../user2/jplusone-report4.txt");
        Path secondOutputFilePath = fileSystem.getPath("/home/user/jplusone-report4.txt");

        TextFileWriter firstTextFileWriter = TextFileWriterManager.getInstance().resolveFileWriter(firstOutputFilePath);
        TextFileWriter secondTextFileWriter = TextFileWriterManager.getInstance().resolveFileWriter(secondOutputFilePath);

        assertThat(firstTextFileWriter, is(not(secondTextFileWriter)));
    }

}