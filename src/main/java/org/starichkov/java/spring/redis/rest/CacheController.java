package org.starichkov.java.spring.redis.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starichkov.java.spring.redis.service.CacheEvictionManager;

/**
 * @author Vadim Starichkov
 * @since 07.08.2020 16:13
 */
@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheEvictionManager cacheEvictionManager;

    @Autowired
    public CacheController(CacheEvictionManager cacheEvictionManager) {
        this.cacheEvictionManager = cacheEvictionManager;
    }

    @DeleteMapping("/all")
    public void evictAll() {
        cacheEvictionManager.evictAll();
    }
}
