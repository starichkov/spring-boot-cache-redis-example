package org.starichkov.java.spring.redis.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starichkov.java.spring.redis.domain.entity.Magazine;

/**
 * @author Vadim Starichkov
 * @since 07.08.2020 16:01
 */
public interface MagazineRepository extends JpaRepository<Magazine, Long> {
}
