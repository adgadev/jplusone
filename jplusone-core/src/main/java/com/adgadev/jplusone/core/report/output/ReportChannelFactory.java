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

import com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties;
import com.adgadev.jplusone.core.properties.JPlusOneProperties.JPlusOneReportProperties.Output;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
public class ReportChannelFactory {

    private final JPlusOneReportProperties reportProperties;

    private final int springContextNumber;

    public ReportChannel createReportChannel(Logger logger) {
        if (reportProperties.getOutput() == Output.LOGGER) {
            return new LoggerReportChannel(logger);

        } else if (reportProperties.getOutput() == Output.STDOUT) {
            return new StdoutReportChannel();

        } else if (reportProperties.getOutput() == Output.FILE) {
            return new FileReportChannel(Paths.get(reportProperties.getFilePath()), springContextNumber);

        } else if (reportProperties.getOutput() == Output.LOGGER_AND_FILE) {
            return new CompositeReportChannel(List.of(
                    new LoggerReportChannel(logger),
                    new FileReportChannel(Paths.get(reportProperties.getFilePath()), springContextNumber)
            ));

        } else {
            throw new IllegalArgumentException("Unsupported report channel " + reportProperties.getOutput());
        }
    }
}
