package com.basaki.data.repository;

import com.basaki.data.entity.Book;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * {@code BookRepository} is a book repository.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/27/17
 */
@Repository
public interface BookRepository {

    /**
     * Saves a book entity. Use the returned book instance for further
     * operations.
     *
     * @param entity the book object to be saved
     * @return the saved book entity
     */
    Book save(Book entity);

    /**
     * Retrieves a book entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    Book findOne(UUID id);

    /**
     * Returns all instances of the book entity.
     *
     * @return all entities
     */
    List<Book> findAll();

    Map<String, String> findAllKeys();

    /**
     * Deletes a given book entity.
     *
     * @param id the identity of book entity to be deleted
     * @throws IllegalArgumentException in case the given entity is {@literal
     *                                  null}.
     */
    void delete(UUID id);


    /**
     * Deletes all book entities.
     */
    void deleteAll();
}
