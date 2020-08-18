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

package com.adgadev.jplusone.core.report;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportSqlFormatterTest {

    @ParameterizedTest
    @MethodSource("sqlDataProvider")
    void shouldFormatSql(String inputSql, String expectedSql) {
        String formattedSql = ReportSqlFormatter.formatSql("    ", inputSql);
        assertEquals(expectedSql, formattedSql);
    }

    private static Stream<Arguments> sqlDataProvider() {
        return Stream.of(
                Arguments.of(
                        "select #SELECT_COLUMNS_LIST from book book0_ " +
                                "where book0_.id = 1",
                        "\n" +
                                "    select [...] from\n" +
                                "        book book0_ \n" +
                                "    where\n" +
                                "        book0_.id = 1"
                ),
                Arguments.of(
                        "select #SELECT_COLUMNS_LIST from author author0_ " +
                                "left outer join genre genre1_ on author0_.genre_id = genre1_.id " +
                                "where author0_.id = 1",
                        "\n" +
                                "    select [...] from\n" +
                                "        author author0_ \n" +
                                "        left outer join genre genre1_ on author0_.genre_id = genre1_.id\n" +
                                "    where\n" +
                                "        author0_.id = 1"
                ),
                Arguments.of(
                        "select #SELECT_COLUMNS_LIST from book book0_ " +
                                "left outer join author author0_ on book0_.author_id = author0_.id " +
                                "left outer join genre genre1_ on author0_.genre_id = genre1_.id " +
                                "where book0_.id = 1 and author0_.name = \"Puzo\"",
                        "\n" +
                                "    select [...] from\n" +
                                "        book book0_ \n" +
                                "        left outer join author author0_ on book0_.author_id = author0_.id\n" +
                                "        left outer join genre genre1_ on author0_.genre_id = genre1_.id\n" +
                                "    where\n" +
                                "        book0_.id = 1 \n" +
                                "        and author0_.name = \"Puzo\""
                ),

                Arguments.of(
                        "update T1 " +
                                "set A1 = 2 " +
                                "where B1 = 'ABC' and C2 in (select C3 from T2 where C4 = 0)",
                        "\n" +
                                "    update\n" +
                                "        T1 \n" +
                                "    set\n" +
                                "        A1 = 2 \n" +
                                "    where\n" +
                                "        B1 = 'ABC' \n" +
                                "        and C2 in (\n" +
                                "            select\n" +
                                "                C3 \n" +
                                "            from\n" +
                                "                T2 \n" +
                                "            where\n" +
                                "                C4 = 0\n" +
                                "        )"
                )
        );
    }

}