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

package com.adgadev.jplusone.core.proxy;

import java.util.concurrent.atomic.AtomicInteger;

public class Identifier {

    private final static AtomicInteger ENTITY_MANAGER_SEQUENCE = new AtomicInteger(0);

    private static final String ENTITY_MANAGER_SEQUENCE_LABEL = "EM";

    private final String label;

    private final int number;

    private Identifier(String sequenceLabel, AtomicInteger sequence) {
        this.label = sequenceLabel;
        this.number = sequence.incrementAndGet();
    }

    public static Identifier nextEntityManagerIdentifier() {
        return new Identifier(ENTITY_MANAGER_SEQUENCE_LABEL, ENTITY_MANAGER_SEQUENCE);
    }

    @Override
    public String toString() {
        return label + "-" + number;
    }
}
