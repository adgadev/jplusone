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

package com.grexdev.jplusone.core.tracking;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum VerbosityLevel { // TODO: enum extending interface
    V0(false, false, false, false, false),
    V1(true, false, false, false, false),
    V2(true, true, false, false, false),
    V3(true, true, true, false, false),
    V4(true, true, true, true, false),
    V5(true, true, true, true, true);

    private final boolean debugModeEnabled;

    private final boolean sessionStackVisible;

    private final boolean transactionStackVisible;

    private final boolean sqlStatementStackVisible;

    private final boolean sqlStatementVisible;

}
