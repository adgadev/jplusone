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
import com.adgadev.jplusone.test.domain.commerce.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class JpaCascadeCrudService {

    private final ManufacturerRepository manufacturerRepository;

    @Transactional
    public Product addManufacturerProduct(Long manufacturerId, String productName) {
        Manufacturer manufacturer = getManufacturerById(manufacturerId);
        Product product = manufacturer.addProduct(productName);
        return product;
    }

    @Transactional
    public void updateManufacturerProductName(Long manufacturerId, String productName, String newProductName) {
        Manufacturer manufacturer = getManufacturerById(manufacturerId);
        Product product = manufacturer.addProduct(productName);
        product.updateName(newProductName);
    }

    @Transactional
    public void deleteManufacturerProduct(Long manufacturerId, String productName) {
        Manufacturer manufacturer = getManufacturerById(manufacturerId);
        Product product = manufacturer.deleteProduct(productName);
    }

    private Manufacturer getManufacturerById(Long manufacturerId) {
        return manufacturerRepository.findById(manufacturerId).get();
    }
}
