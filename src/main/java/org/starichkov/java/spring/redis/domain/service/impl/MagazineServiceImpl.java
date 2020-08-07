package org.starichkov.java.spring.redis.domain.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.starichkov.java.spring.redis.domain.entity.Magazine;
import org.starichkov.java.spring.redis.domain.repository.MagazineRepository;
import org.starichkov.java.spring.redis.domain.service.MagazineService;
import org.starichkov.java.spring.redis.service.CacheEvictionManager;
import org.starichkov.java.spring.redis.service.Constants;

/**
 * @author Vadim Starichkov
 * @since 07.08.2020 16:05
 */
@Service
@Slf4j
public class MagazineServiceImpl implements MagazineService {

    private final MagazineRepository repository;
    private final CacheEvictionManager cacheEvictionManager;

    @Autowired
    public MagazineServiceImpl(MagazineRepository repository, CacheEvictionManager cacheEvictionManager) {
        this.repository = repository;
        this.cacheEvictionManager = cacheEvictionManager;
    }

    @Cacheable(cacheNames = Constants.CACHE_MAGAZINES_ID, key = "#id", unless = "#result == null")
    @Override
    public Magazine get(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Magazine create(Magazine book) {
        return repository.save(book);
    }

    @CachePut(cacheNames = Constants.CACHE_MAGAZINES_ID, key = "#book.id", unless = "#result == null")
    @Override
    public Magazine update(Magazine book) {
        return repository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).ifPresent(this::deleteMagazine);
    }

    private void deleteMagazine(Magazine magazine) {
//        repository.delete(magazine);
        cacheEvictionManager.evictMagazine(magazine.getId());
    }
}
