package org.starichkov.java.spring.redis.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.starichkov.java.spring.redis.domain.entity.Book;
import org.starichkov.java.spring.redis.domain.repository.BookRepository;
import org.starichkov.java.spring.redis.service.CacheEvictionManager;
import org.starichkov.java.spring.redis.service.Constants;

/**
 * @author Vadim Starichkov
 * @since 16.07.2020 16:28
 */
@Service
@Slf4j
public class BookService {

    private final BookRepository repository;
    private final CacheEvictionManager cacheEvictionManager;

    @Autowired
    public BookService(BookRepository repository, CacheEvictionManager cacheEvictionManager) {
        this.repository = repository;
        this.cacheEvictionManager = cacheEvictionManager;
    }

    @Cacheable(cacheNames = Constants.CACHE_BOOKS_ID, key = "#id", unless = "#result == null")
    public Book get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Cacheable(cacheNames = Constants.CACHE_BOOKS_ISBN, key = "#isbn", unless = "#result == null")
    public Book getByISBN(String isbn) {
        return repository.findByIsbn(isbn).orElse(null);
    }

    //@CachePut(key = "#result.id", unless = "#result == null")
    public Book create(Book book) {
        return repository.save(book);
    }

    @Caching(put = {
            @CachePut(cacheNames = Constants.CACHE_BOOKS_ID, key = "#book.id", unless = "#result == null"),
            @CachePut(cacheNames = Constants.CACHE_BOOKS_ISBN, key = "#book.isbn", unless = "#result == null")
    })
    public Book update(Book book) {
        return repository.save(book);
    }

    public void deleteById(Long id) {
        repository.findById(id).ifPresent(this::deleteBook);
    }

    public void deleteByIsbn(String isbn) {
        repository.findByIsbn(isbn).ifPresent(this::deleteBook);
    }

    private void deleteBook(Book book) {
//        repository.delete(book);
        cacheEvictionManager.evict(book.getId(), book.getIsbn());
    }

    public void evictAll() {
        cacheEvictionManager.evictAll();
    }
}
