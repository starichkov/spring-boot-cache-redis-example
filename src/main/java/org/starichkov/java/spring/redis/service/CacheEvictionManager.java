package org.starichkov.java.spring.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * @author Vadim Starichkov
 * @since 28.07.2020 12:11
 */
@Service
@Slf4j
public class CacheEvictionManager {

    @Caching(evict = {
            @CacheEvict(cacheNames = Constants.CACHE_BOOKS_ID, key = "#id"),
            @CacheEvict(cacheNames = Constants.CACHE_BOOKS_ISBN, key = "#isbn")
    })
    public void evict(Long id, String isbn) {
        log.info("Cache: evicting by id {} and isbn {}", id, isbn);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = Constants.CACHE_BOOKS_ID, allEntries = true),
            @CacheEvict(cacheNames = Constants.CACHE_BOOKS_ISBN, allEntries = true)
    })
    public void evictAll() {
        log.info("Cache: evicting all entries");
    }
}
