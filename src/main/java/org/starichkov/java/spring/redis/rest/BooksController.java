package org.starichkov.java.spring.redis.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.starichkov.java.spring.redis.domain.entity.Book;
import org.starichkov.java.spring.redis.domain.service.BookService;

/**
 * @author Vadim Starichkov
 * @since 16.07.2020 16:36
 */
@RestController
@RequestMapping("/books")
public class BooksController {

    private final BookService service;

    @Autowired
    public BooksController(BookService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @PostMapping(params = {"title", "author"})
    public Book createBook(@RequestParam("title") String title, @RequestParam("author") String author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        return service.create(book);
    }

    @PatchMapping(path = "/{id}", params = {"title"})
    public Book updateBook(@PathVariable("id") Long id, @RequestParam("title") String title) {
        Book book = service.get(id);
        book.setTitle(title);
        return service.update(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        service.deleteById(id);
    }

    @DeleteMapping("/all")
    public void evictAll() {
        service.evictAll();
    }
}
