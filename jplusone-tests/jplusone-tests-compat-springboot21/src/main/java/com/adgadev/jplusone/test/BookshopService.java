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

package com.adgadev.jplusone.test;

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
}
