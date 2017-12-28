package com.basaki.service;

import com.basaki.data.entity.Book;
import com.basaki.data.repository.BookRepository;
import com.basaki.error.exception.DataNotFoundException;
import com.basaki.model.BookRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@code BookService} provides CRUD functioanality on book.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/27/17
 */
@Service
public class BookService {

    private final BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book create(BookRequest request) {
        Book entity = new Book();
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());

        return repository.save(entity);
    }

    public Book read(UUID id) {
        Book book = repository.findOne(id);

        if (book == null) {
            throw new DataNotFoundException("No book found with ID " + id);
        }

        return book;
    }

    public List<Book> readAll() {
        List<Book> books = repository.findAll();

        if (books == null || books.isEmpty()) {
            throw new DataNotFoundException("No books found!");
        }

        return books;
    }

    public void delete(UUID id) {
        repository.delete(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}