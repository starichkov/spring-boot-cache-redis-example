package org.starichkov.java.spring.redis.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Vadim Starichkov
 * @since 16.07.2020 15:11
 */
@Entity
@Table(name = "books", schema = "book_store")
@Getter
@Setter
public class Book {

    public Book() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "title")
    private String title;

    @Column(name = "isbn")
    private String isbn;
}
