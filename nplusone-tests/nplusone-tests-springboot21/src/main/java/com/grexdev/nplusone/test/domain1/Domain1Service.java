package com.grexdev.nplusone.test.domain1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@Slf4j
@Service
@RequiredArgsConstructor
class Domain1Service {

    private final DomainARepository domainARepository;

    @Transactional
    public List<String> getDataFromAAndFetchDataFromB() {
        Optional<DomainA> domainA1 = domainARepository.findById(1L);
        DomainA domainA = domainA1.get();

        domainA.getDomainB().action();
        return asList(domainA.getName(), domainA.getDomainB().getName());
    }
}
