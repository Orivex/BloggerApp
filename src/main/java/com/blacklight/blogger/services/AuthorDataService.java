package com.blacklight.blogger.services;

import com.blacklight.blogger.entities.Author;
import org.springframework.stereotype.Service;

@Service
public class AuthorDataService {
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthorName(Author author) {
        this.author = author;
    }
}