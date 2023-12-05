package com.blacklight.blogger.dao;

import com.blacklight.blogger.entities.Author;
import com.blacklight.blogger.entities.Blog;

import java.util.List;

public interface AuthorDAO extends DAO<Author> {
    Author getAuthorByName(String name);
}
