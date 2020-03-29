package com.grexdev.nplusone.test.domain.bookshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class BookshopServiceTest {

    @Autowired
    private BookshopService bookshopService;

    @Test
    void shouldFetchUsingLazyLoading() {
        BookDto bookDto = bookshopService.getSampleBookDetailsUsingLazyLoading();
        assertEquals(new BookDto("Mario Puzo", "Godfather"), bookDto);
    }

    @Test
    void shouldFetchUsingEagerLoading() {
        BookDto bookDto = bookshopService.getSampleBookDetailsUsingEagerLoading();
        assertEquals(new BookDto("Mario Puzo", "Godfather"), bookDto);
    }

    @Test
    void shouldRunActionOnProxyEntity() {
        bookshopService.runActionOnSampleAuthorProxy();
    }
}
