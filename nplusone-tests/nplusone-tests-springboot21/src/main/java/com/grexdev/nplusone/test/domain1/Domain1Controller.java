package com.grexdev.nplusone.test.domain1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
class Domain1Controller {

    private final Domain1Service service;

    @GetMapping("/testcase1")
    List<String> endpoint1() {
        return service.getDataFromAAndFetchDataFromB();
    }

}
