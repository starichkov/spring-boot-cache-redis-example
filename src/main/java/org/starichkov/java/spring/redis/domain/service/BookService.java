package org.starichkov.java.spring.redis.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.starichkov.java.spring.redis.domain.entity.Book;
import org.starichkov.java.spring.redis.domain.repository.BookRepository;

/**
 * @author Vadim Starichkov
 * @since 16.07.2020 16:28
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"books"})
public class BookService {

    private final BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public Book get(Long id) {
        return repository.findById(id).orElse(null);
    }

    //@CachePut(key = "#result.id", unless = "#result == null")
    public Book create(Book book) {
        return repository.save(book);
    }

    @CachePut(key = "#book.id", unless = "#result == null")
    public Book update(Book book) {
        return repository.save(book);
    }

    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @CacheEvict(allEntries = true)
    public void evictAll() {
        log.info("Cache: evicting all entries");
    }
}
