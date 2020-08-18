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

package com.grexdev.jplusone.test.domain.commerce.session;

import com.grexdev.jplusone.test.domain.commerce.Manufacturer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class InnerSessionService {

    private final EntityManager entityManager;

    @Transactional
    public Manufacturer fetchDataInExistingTransaction(Long id) {
        return entityManager.find(Manufacturer.class, id);
    }

    @Transactional(REQUIRES_NEW)
    public Manufacturer fetchDataInNewTransaction(Long id) {
        return entityManager.find(Manufacturer.class, id);
    }

}
