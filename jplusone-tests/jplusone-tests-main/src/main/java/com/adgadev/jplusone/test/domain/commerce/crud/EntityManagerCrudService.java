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

package com.adgadev.jplusone.test.domain.commerce.crud;

import com.adgadev.jplusone.test.domain.commerce.Manufacturer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EntityManagerCrudService {

    private final EntityManager entityManager;

    @Transactional
    public void addManufacturer(Long id, String name) {
        Manufacturer manufacturer = Manufacturer.of(id, name);
        entityManager.persist(manufacturer);
    }

    @Transactional
    public void updateManufacturerNameByMerge(Long id, String name) {
        Manufacturer manufacturer = Manufacturer.of(id, name);
        entityManager.merge(manufacturer);
    }

    @Transactional
    public void updateManufacturerNameOnManagedEntity(Long id, String name) {
        Manufacturer manufacturer = entityManager.find(Manufacturer.class, id);
        manufacturer.updateName(name);
    }

    @Transactional
    public void deleteManufacturer(Long id) {
        Manufacturer manufacturer = entityManager.find(Manufacturer.class, id);
        entityManager.remove(manufacturer);
    }

}
