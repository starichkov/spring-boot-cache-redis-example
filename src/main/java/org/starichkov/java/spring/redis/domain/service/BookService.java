package org.starichkov.java.spring.redis.domain.service;

import org.starichkov.java.spring.redis.domain.entity.Book;

/**
 * @author Vadim Starichkov
 * @since 16.07.2020 16:28
 */
public interface BookService {

    Book get(Long id);

    Book getByISBN(String isbn);

    Book create(Book book);

    Book update(Book book);

    void deleteById(Long id);

    void deleteByIsbn(String isbn);
}
