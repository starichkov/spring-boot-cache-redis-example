package org.starichkov.java.spring.redis.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starichkov.java.spring.redis.domain.entity.Book;

/**
 * @author Vadim Starichkov
 * @since 16.07.2020 16:27
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}