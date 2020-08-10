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

package com.grexdev.jplusone.test.domain.commerce.crud;

import com.grexdev.jplusone.test.domain.commerce.Manufacturer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class JpaRepositoryCrudService {

    private final ManufacturerRepository manufacturerRepository;

    @Transactional
    public void addManufacturer(Long id, String name) {
        Manufacturer manufacturer = Manufacturer.of(id, name);
        manufacturerRepository.save(manufacturer);
    }

    @Transactional
    public void updateManufacturerNameByMerge(Long id, String name) {
        Manufacturer manufacturer = Manufacturer.of(id, name);
        manufacturerRepository.save(manufacturer);
    }

    @Transactional
    public void updateManufacturerNameOnManagedEntity(Long id, String name) {
        Manufacturer manufacturer = manufacturerRepository.findById(id).get();
        manufacturer.updateName(name);
    }

    @Transactional
    public void deleteManufacturer(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id).get();
        manufacturerRepository.delete(manufacturer);
    }

    @Transactional
    public void deleteManufacturerById(Long id) {
        manufacturerRepository.deleteById(id);
    }

}
