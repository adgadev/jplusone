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
