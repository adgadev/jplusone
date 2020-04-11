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

package com.grexdev.jplusone.test.domain.commerce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class CommerceService {

    private final EntityManager entityManager;

    public void getClient() {
        Client client = entityManager.find(Client.class, 1L);
        User user = client.getUser();

        ClientProfile clientProfile = client.getClientProfile();
        clientProfile.getPhotoLink();
        clientProfile.getClient();

        Set<Order> orders = client.getOrders();
        Order order = orders.iterator().next();

        Set<Product> products = order.getProducts();
        Product product = products.iterator().next();

        product.getManufacturer().getName();
        log.info("client: {}", client);
    }

}
