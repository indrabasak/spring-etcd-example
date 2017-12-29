package com.basaki.data.repository;

import com.basaki.data.entity.Book;
import com.basaki.error.exception.DatabaseException;
import com.coreos.jetcd.Client;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.DeleteResponse;
import com.coreos.jetcd.kv.GetResponse;
import com.coreos.jetcd.options.DeleteOption;
import com.coreos.jetcd.options.GetOption;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@code BookRepositoryImpl} is an implementation book repository. It servers
 * as an example for etcd repository.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/27/17
 */
@Component
@Slf4j
public class BookRepositoryImpl implements BookRepository {

    private static final String PREFIX = "Books";

    private Client client;

    private ObjectMapper mapper;

    @Autowired
    public BookRepositoryImpl(Client client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity shouldn't be null!");
        }

        entity.setId(UUID.randomUUID());

        try {
            String key = PREFIX + entity.getId().toString();
            client.getKVClient().put(
                    ByteSequence.fromString(key),
                    ByteSequence.fromString(
                            mapper.writeValueAsString(entity))).get();

            log.info("Created new book with ID " + entity.getId());
        } catch (Exception e) {
            throw new DatabaseException("Failed to insert entity " + entity,
                    e);
        }

        return entity;
    }

    @Override
    public Book findOne(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID shouldn't be null!");
        }

        try {
            String key = PREFIX + id.toString();
            CompletableFuture<GetResponse> futureResponse =
                    client.getKVClient().get(
                            ByteSequence.fromString(key));

            GetResponse response = futureResponse.get();
            if (response.getKvs().isEmpty()) {
                log.info("Failed to retrieve any book with ID " + id);
                return null;
            }

            log.info("Retrieved book with ID " + id);
            return mapper.readValue(
                    response.getKvs().get(0).getValue().toStringUtf8(),
                    Book.class);

        } catch (Exception e) {
            throw new DatabaseException(
                    "Failed to retrieve entity with ID " + id,
                    e);
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            ByteSequence key = ByteSequence.fromString(PREFIX);
            GetOption option = GetOption.newBuilder()
                    .withSortField(GetOption.SortTarget.KEY)
                    .withSortOrder(GetOption.SortOrder.DESCEND)
                    .withPrefix(key)
                    .build();

            CompletableFuture<GetResponse> futureResponse =
                    client.getKVClient().get(key, option);

            GetResponse response = futureResponse.get();
            if (response.getKvs().isEmpty()) {
                log.info("Failed to retrieve any book.");
                return new ArrayList<>();
            }


            List<Book> books = new ArrayList<>();
            for (KeyValue kv : response.getKvs()) {
                books.add(mapper.readValue(kv.getValue().toStringUtf8(),
                        Book.class));
            }

            log.info("Retrieved " + response.getKvs().size() + " books.");

            return books;

        } catch (Exception e) {
            throw new DatabaseException(
                    "Failed to retrieve entities", e);
        }
    }

    @Override
    public Map<String, String> findAllKeys() {
        try {
            ByteSequence key = ByteSequence.fromString("\0");
            GetOption option = GetOption.newBuilder()
                    .withSortField(GetOption.SortTarget.KEY)
                    .withSortOrder(GetOption.SortOrder.DESCEND)
                    .withRange(key)
                    .build();

            CompletableFuture<GetResponse> futureResponse =
                    client.getKVClient().get(key, option);

            GetResponse response = futureResponse.get();
            if (response.getKvs().isEmpty()) {
                log.info("Failed to retrieve any keys.");
                return null;
            }


            Map<String, String> keyValueMap = new HashMap<>();
            for (KeyValue kv : response.getKvs()) {
                keyValueMap.put(kv.getKey().toStringUtf8(),
                        kv.getValue().toStringUtf8());
            }

            log.info("Retrieved " + response.getKvs().size() + " keys.");

            return keyValueMap;

        } catch (Exception e) {
            throw new DatabaseException(
                    "Failed to retrieve any keys.", e);
        }
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID shouldn't be null!");
        }

        try {
            DeleteResponse response = client.getKVClient().delete(
                    ByteSequence.fromString(id.toString())).get();

            if (response.getDeleted() == 1) {
                log.info("Deleted book with ID " + id);
            } else {
                throw new DatabaseException(
                        "Failed to delete book with ID " + id);
            }
        } catch (Exception e) {
            throw new DatabaseException(
                    "Failed to delete book with ID " + id, e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            ByteSequence key = ByteSequence.fromString(PREFIX);
            DeleteOption deleteOpt = DeleteOption.newBuilder()
                    .withPrefix(key).build();

            DeleteResponse response =
                    client.getKVClient().delete(key, deleteOpt).get();

            log.info("Deleted " + response.getDeleted() + " number of books.");
        } catch (Exception e) {
            throw new DatabaseException(
                    "Failed to delete all books.", e);
        }
    }
}
