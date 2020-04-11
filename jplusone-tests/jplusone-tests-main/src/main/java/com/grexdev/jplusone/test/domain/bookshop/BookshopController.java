package com.grexdev.jplusone.test.domain.bookshop;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class BookshopController {

    private final BookshopService bookshopService;

    @GetMapping("/book/lazy")
    BookDto getSampleBookUsingLazyLoading() {
        return bookshopService.getSampleBookDetailsUsingLazyLoading();
    }

    @GetMapping("/book/eager")
    BookDto getSampleBookUsingEagerLoading() {
        return bookshopService.getSampleBookDetailsUsingEagerLoading();
    }
}
