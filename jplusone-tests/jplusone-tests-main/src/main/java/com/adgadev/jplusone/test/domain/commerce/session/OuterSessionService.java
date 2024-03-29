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

package com.adgadev.jplusone.test.domain.commerce.session;

import com.adgadev.jplusone.test.domain.commerce.Manufacturer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OuterSessionService {

    private final InnerSessionService innerSessionService;

    private final EntityManager entityManager;

    @Transactional
    public boolean checkIfEntityManagerOpenInTx() {
        return entityManager.isOpen();
    }

    public boolean checkIfEntityManagerOpenNoTx() {
        return entityManager.isOpen();
    }

    @Transactional
    public Query createQueryInTx() {
        return entityManager.createQuery("FROM Product p");
    }

    public Query createQueryNoTx() {
        return entityManager.createQuery("FROM Product p");
    }

    @Transactional
    public void fetchDataOuterTx() {
        fetchData(1L);
    }

    @Transactional
    public void fetchDataInnerNewTx() {
        innerSessionService.fetchDataInNewTransaction(2L);
    }

    @Transactional
    public void fetchDataOuterTxInnerTx() {
        fetchData(1L);
        innerSessionService.fetchDataInExistingTransaction(2L);
    }

    @Transactional
    public void fetchDataOuterTxInnerNewTx() {
        fetchData(1L);
        innerSessionService.fetchDataInNewTransaction(2L);
    }

    public void fetchDataOuterNoTxInnerTx() {
        fetchData(1L);
        innerSessionService.fetchDataInExistingTransaction(2L);
    }

    public void fetchDataOuterNoTxInnerNewTx() {
        fetchData(1L);
        innerSessionService.fetchDataInNewTransaction(2L);
    }

    @Transactional
    public void fetchDataInnerTxOuterTx() {
        innerSessionService.fetchDataInExistingTransaction(2L);
        fetchData(1L);
    }

    @Transactional
    public void fetchDataInnerNewTxOuterTx() {
        innerSessionService.fetchDataInNewTransaction(2L);
        fetchData(1L);
    }

    @Transactional
    public void fetchDataOuterTxInnerNewTxOuterTx() {
        fetchData(1L);
        innerSessionService.fetchDataInNewTransaction(2L);
        fetchData(3L);
    }

    private Manufacturer fetchData(Long id) {
        return entityManager.find(Manufacturer.class, id);
    }

}
