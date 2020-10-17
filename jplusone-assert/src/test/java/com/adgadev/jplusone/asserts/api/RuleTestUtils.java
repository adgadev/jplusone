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

package com.adgadev.jplusone.asserts.api;

import com.adgadev.jplusone.asserts.context.stub.SessionNodeStub;
import org.junit.jupiter.api.function.Executable;

import static com.adgadev.jplusone.asserts.context.mother.RootNodeMother.anyRootNode;
import static com.adgadev.jplusone.asserts.impl.JPlusOneAssertionContextMother.anyContext;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuleTestUtils {

    static Executable expectMatchingRule(JPlusOneAssertionRule rule, SessionNodeStub session) {
        return () -> rule.check(anyContext(anyRootNode(session)));
    }

    static Executable expectFailingRule(JPlusOneAssertionRule rule, SessionNodeStub session) {
        return () -> assertThrows(AssertionError.class, () ->
                rule.check(anyContext(anyRootNode(session)))
        );
    }
}
