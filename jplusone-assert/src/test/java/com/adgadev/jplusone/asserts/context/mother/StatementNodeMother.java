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

package com.adgadev.jplusone.asserts.context.mother;

import com.adgadev.jplusone.asserts.context.stub.StatementNodeStub;
import com.adgadev.jplusone.core.registry.StatementNodeView;
import com.adgadev.jplusone.core.registry.StatementType;

public class StatementNodeMother {

    public static StatementNodeStub anyStatementNode() {
        return anySelectStatementNode();
    }

    public static StatementNodeStub anyStatementNode(StatementType statementType) {
        return StatementNodeStub.builder()
                .statementType(statementType)
                .sql(">>>HERE GOES SOME SQL STATEMENT<<<")
                .build();
    }

    public static StatementNodeStub anySelectStatementNode() {
        return StatementNodeStub.builder()
                .statementType(StatementType.SELECT)
                .sql("SELECT * FROM books WHERE id=1")
                .build();
    }

    public static StatementNodeStub anyInsertStatementNode() {
        return StatementNodeStub.builder()
                .statementType(StatementType.INSERT)
                .sql("INSERT INTO books(id, title) VALUES (1, 'Godfather'")
                .build();
    }

    public static StatementNodeStub anyUpdateStatementNode() {
        return StatementNodeStub.builder()
                .statementType(StatementType.UPDATE)
                .sql("UPDATE books SET title='Godfather' WHERE id=1")
                .build();
    }
}
