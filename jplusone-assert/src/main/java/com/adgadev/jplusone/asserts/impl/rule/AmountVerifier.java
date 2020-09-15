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

package com.adgadev.jplusone.asserts.impl.rule;

import com.adgadev.jplusone.asserts.api.builder.AmountMatcher;
import com.adgadev.jplusone.asserts.impl.rule.message.AmountAssertionMessageTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class AmountVerifier {

    private final AmountMatcher matcher;

    private final String expectedAmountText;

    private final Integer expectedAmountValue;

    void checkAmount(int amount, Supplier<AmountAssertionMessageTemplate> messageTemplateSupplier) {
        if (!matcher.apply(amount)) {
            String expectedAmount = expectedAmountValue != null
                    ? String.format("%s <%d>", expectedAmountText, expectedAmountValue)
                    : expectedAmountText;

            AmountAssertionMessageTemplate messageTemplate = messageTemplateSupplier.get();
            String errorMessage = messageTemplate.buildMessage(expectedAmount, amount);
            throw new AssertionError(errorMessage);
        }
    }

}
