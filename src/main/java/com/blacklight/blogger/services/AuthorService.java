package com.blacklight.blogger.services;

import com.blacklight.blogger.entities.Blog;
import com.blacklight.blogger.responses.AuthorResponse;
import com.blacklight.blogger.entities.Author;
import com.blacklight.blogger.dao.AuthorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    public AuthorDAO authorDAO;

    public Author getAuthorById(Long id) {
        return authorDAO.getById(id);
    }

    public List<Author> getAllAuthors() {
        return authorDAO.getAll();
    }

    public Author getAuthorByName(String name) {
        return authorDAO.getAuthorByName(name);
    }

    public List<Blog> getAuthorsBlogs(Long id) {
        Author author = authorDAO.getById(id);
        return author.getBlogs();
    }

    public List<String> getAuthorsTitles(Long id) {
        Author author = authorDAO.getById(id);
        List<String> titles = new ArrayList<>();

        for (Blog blog : author.getBlogs()) {
            titles.add(blog.getTitle());
        }

        return titles;
    }

    boolean authorNameExists(String name) {
        if(getAuthorByName(name) == null) {
            return false;
        }

        return true;
    }

    public AuthorResponse loginAuthorAcc(Author author) {
        if(authorDAO.getAuthorByName(author.getName()) == null) {
            return AuthorResponse.NAME_NOT_FOUND;
        }

        return checkPassword(author);
    }

    public AuthorResponse createAuthorAcc(Author author) {
        if(authorNameExists(author.getName()) == true) {
            return AuthorResponse.NAME_ALREADY_EXISTS;
        }

        if(isValidName(author.getName()) == false) {
            return AuthorResponse.NAME_TOO_SHORT;
        }

        authorDAO.save(author);
        return AuthorResponse.OK;
    }

    public void delete(Author author) {
        authorDAO.delete(author);
    }

    public AuthorResponse checkPassword(Author author) {
        if(!author.getPassword().equals(getAuthorByName(author.getName()).getPassword())) {
            return AuthorResponse.WRONG_PASSWORD;
        }

        return AuthorResponse.OK;
    }

    boolean isValidName(String name) {
        if(name.length() <= 2) {
            return false;
        }

        return true;
    }

}
