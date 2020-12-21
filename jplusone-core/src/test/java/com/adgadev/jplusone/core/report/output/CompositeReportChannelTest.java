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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CompositeReportChannelTest {

    @Test
    public void shouldDelegateCallsToAllChildChannels() {
        // given
        ReportChannel reportChannel1 = mock(ReportChannel.class);
        ReportChannel reportChannel2 = mock(ReportChannel.class);
        ReportChannel compositeReportChannel = new CompositeReportChannel(List.of(reportChannel1, reportChannel2));

        // when
        compositeReportChannel.printMessage(MessageType.REPORT, "message");

        // then
        verify(reportChannel1).printMessage(MessageType.REPORT, "message");
        verify(reportChannel2).printMessage(MessageType.REPORT, "message");
    }
}