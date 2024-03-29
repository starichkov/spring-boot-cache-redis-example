package org.starichkov.java.spring.redis.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Vadim Starichkov
 * @since 07.08.2020 15:57
 */
@Entity
@Table(name = "magazines", schema = "book_store")
@Data
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "issue")
    private Integer issue;
}
