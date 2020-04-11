package com.grexdev.jplusone.test.domain.bookshop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class BookshopService {

    private final BookRepository bookRepository;

    public BookDto getSampleBookDetailsUsingLazyLoading() {
        Book book = bookRepository.findById(1L).get();
        String authorName = book.getAuthor().getName();

        return new BookDto(authorName, book.getTitle());
    }

    public BookDto getSampleBookDetailsUsingEagerLoading() {
        Book book = bookRepository.findByIdAndFetchAuthor(1L).get();
        String authorName = book.getAuthor().getName();

        return new BookDto(authorName, book.getTitle());
    }

    public void runActionOnSampleAuthorProxy() {
        Book book = bookRepository.findById(1L).get();
        Author author = book.getAuthor();
        log.debug("Running action");

        author.runAction(); // should trigger fetch
    }

}
