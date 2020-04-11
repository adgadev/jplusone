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

package com.grexdev.jplusone.core.report;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportSqlFormatterTest {

    @ParameterizedTest
    @MethodSource("sqlDataProvider")
    void shouldFormatSql(String inputSql, String expectedSql) {
        String formattedSql = ReportSqlFormatter.formatSql("\t", inputSql);
        assertEquals(expectedSql, formattedSql);
    }

    private static Stream<Arguments> sqlDataProvider() {
        return Stream.of(
                Arguments.of(
                        "select [...] from book book0_ " +
                                "where book0_.id = 1",
                        "\n" +
                                "\tselect [...] from\n" +
                                "\t\t book book0_\n" +
                                "\twhere\n" +
                                "\t\t book0_.id = 1"
                ),
                Arguments.of(
                        "select [...] from author author0_ " +
                                "left outer join genre genre1_ on author0_.genre_id = genre1_.id " +
                                "where author0_.id = 1",
                        "\n" +
                                "\tselect [...] from\n" +
                                "\t\t author author0_\n" +
                                "\t\t left outer join genre genre1_ on author0_.genre_id = genre1_.id\n" +
                                "\twhere\n" +
                                "\t\t author0_.id = 1"
                ),
                Arguments.of(
                        "select [...] from book book0_ " +
                                "left outer join author author0_ on book0_.author_id = author0_.id " +
                                "left outer join genre genre1_ on author0_.genre_id = genre1_.id " +
                                "where book0_.id = 1 and author0_.name = \"Puzo\"",
                        "\n" +
                                "\tselect [...] from\n" +
                                "\t\t book book0_\n" +
                                "\t\t left outer join author author0_ on book0_.author_id = author0_.id\n" +
                                "\t\t left outer join genre genre1_ on author0_.genre_id = genre1_.id\n" +
                                "\twhere\n" +
                                "\t\t book0_.id = 1\n" +
                                "\t\t and author0_.name = \"Puzo\""
                )
        );
    }

}