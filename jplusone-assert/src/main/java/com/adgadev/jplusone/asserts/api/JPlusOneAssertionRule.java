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

import com.adgadev.jplusone.asserts.api.builder.SessionSelectorBuilder;
import com.adgadev.jplusone.asserts.impl.SessionSelectorBuilderFactory;

public interface JPlusOneAssertionRule {

    static SessionSelectorBuilder within() {
        return new SessionSelectorBuilderFactory().create();
    }

    void check(JPlusOneAssertionContext context);
}
