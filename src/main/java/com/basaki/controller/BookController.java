package com.basaki.controller;

import com.basaki.data.entity.Book;
import com.basaki.model.BookRequest;
import com.basaki.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code BookController} exposes book service.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/27/17
 */
@RestController
@Slf4j
@Api(value = "Book Service", produces = "application/json", tags = {"1"})
public class BookController {

    private BookService service;

    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }

    @ApiOperation(value = "Creates a new book.",
            response = Book.class)
    @RequestMapping(method = RequestMethod.POST, value = "/books")
    public Book create(@RequestBody BookRequest request) {
        return service.create(request);
    }

    @ApiOperation(value = "Retrieves a book based on identifier.",
            response = Book.class)
    @RequestMapping(method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE}, value = "/books/{id}")
    public Book read(@PathVariable("id") UUID id) {
        return service.read(id);
    }

    @ApiOperation(value = "Retrieves all books.",
            response = Book.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE}, value = "/books")
    public List<Book> readAll() {
        return service.readAll();
    }

    @ApiOperation(value = "Deletes a book based on identifier.")
    @RequestMapping(method = RequestMethod.DELETE, value = "/books/{id}")
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @ApiOperation(value = "Deletes all books.")
    @RequestMapping(method = RequestMethod.DELETE, value = "/books")
    public void deleteAll() {
        service.deleteAll();
    }

    @ApiOperation(value = "Retrieves all keys.", responseContainer = "Map")
    @RequestMapping(method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE}, value = "/keys")
    public Map<String, String> readAllKeys() {
        return service.readEverything();
    }
}
