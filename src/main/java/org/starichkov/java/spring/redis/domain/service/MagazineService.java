package org.starichkov.java.spring.redis.domain.service;

import org.starichkov.java.spring.redis.domain.entity.Magazine;

/**
 * @author Vadim Starichkov
 * @since 07.08.2020 16:02
 */
public interface MagazineService {

    Magazine get(Long id);

    Magazine create(Magazine book);

    Magazine update(Magazine book);

    void deleteById(Long id);
}
