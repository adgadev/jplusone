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

package com.adgadev.jplusone.asserts.impl.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public class CollectionUtils {

    public static <T> T getLastElement(Collection<T> collection) {
        return collection.stream()
                .skip(Math.max(collection.size() - 1, 0))
                .findFirst()
                .orElse(null);
    }

    public static <T> List<T> getMaxCommonHeadFragmentOfLists(List<T> firstList, List<T> secondList) {
        Iterator<T> firstIterator = firstList.iterator();
        Iterator<T> secondIterator = secondList.iterator();
        int amountOfSameItems = 0;
        boolean differenceFound = false;

        while (firstIterator.hasNext() && secondIterator.hasNext() && !differenceFound) {
            T firstListItem = firstIterator.next();
            T secondListItem = secondIterator.next();

            if (Objects.equals(firstListItem, secondListItem)) {
                amountOfSameItems++;
            } else {
                differenceFound = true;
            }
        }

        return amountOfSameItems == 0 ? emptyList() : firstList.subList(0, amountOfSameItems);
    }
}
