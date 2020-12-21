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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class StdoutReportChannelTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldPrintReportMessage() {
        // given
        ReportChannel channel = new StdoutReportChannel();
        String logContent = "\nFirst line\nSecond line\n";

        // when
        channel.printMessage(MessageType.REPORT, logContent);

        // then
        assertThat(outContent.toString(), equalTo(logContent + "\n"));
    }

    @Test
    public void shouldPrintTextMessage() {
        // given
        ReportChannel channel = new StdoutReportChannel();
        String logContent = "Simple text message";

        // when
        channel.printMessage(MessageType.TEXT, logContent);

        // then
        assertThat(outContent.toString(), equalTo(logContent + "\n"));
    }
}