package com.basaki.data.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;


/**
 * {@code Book} represents a book entity.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/7/17
 */
@Data
public class Book implements Serializable {

    private UUID id;

    private String title;

    private String author;
}