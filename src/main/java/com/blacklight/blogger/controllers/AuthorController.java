package com.blacklight.blogger.controllers;

import com.blacklight.blogger.responses.AuthorResponse;
import com.blacklight.blogger.responses.BlogResponse;
import com.blacklight.blogger.entities.Author;
import com.blacklight.blogger.entities.Blog;
import com.blacklight.blogger.exceptions.ApiRequestException;
import com.blacklight.blogger.services.AuthorService;
import com.blacklight.blogger.services.BlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BlogService blogService;


    public AuthorController(AuthorService authorService, BlogService blogService) {
        this.authorService = authorService;
        this.blogService = blogService;
    }

    @PostMapping("/createAuthorAcc")
    @ResponseBody
    public void createAuthorAcc(@RequestBody Author author) {
        switch (authorService.createAuthorAcc(author)) {
            case NAME_ALREADY_EXISTS -> throw new ApiRequestException(AuthorResponse.NAME_ALREADY_EXISTS.toString());
            case NAME_TOO_SHORT -> throw new ApiRequestException(AuthorResponse.NAME_TOO_SHORT.toString());
        }
    }

    @PostMapping("/loginAuthorAcc")
    @ResponseBody
    public void loginAuthorAcc(@RequestBody Author author) {
        switch (authorService.loginAuthorAcc(author)) {
            case WRONG_PASSWORD -> throw new ApiRequestException(AuthorResponse.WRONG_PASSWORD.toString());
            case NAME_NOT_FOUND -> throw new ApiRequestException(AuthorResponse.NAME_NOT_FOUND.toString());
        }
    }

    @GetMapping("/getAuthorsBlogs/{id}")
    public List<Blog> getAuthorsBlogs(@PathVariable Long id) {
        return authorService.getAuthorsBlogs(id);
    }

    @GetMapping("/getAuthorsTitles/{id}")
    public List<String> getAuthorsTitles(@PathVariable Long id) {
        return authorService.getAuthorsTitles(id);
    }

    @GetMapping("/getAuthorByName")
    public Author getAuthorByName(String authorName) {
        return authorService.getAuthorByName(authorName);
    }

    @PostMapping("/postBlog/{id}")
    @ResponseBody
    public String postBlog(@RequestBody Blog blog, @PathVariable Long id) {

        Author author = authorService.getAuthorById(id);
        blog.setAuthor(author);

        switch (blogService.saveBlog(blog)) {
            case TITLE_ALREADY_EXISTS -> throw new ApiRequestException(BlogResponse.TITLE_ALREADY_EXISTS.toString());
            case TITLE_TOO_LONG -> throw new ApiRequestException(BlogResponse.TITLE_TOO_LONG.toString());
            case TITLE_TOO_SHORT -> throw new ApiRequestException(BlogResponse.TITLE_TOO_SHORT.toString());
        }

        author.getBlogs().add(blog);

        return "Your blog was successfully posted!";
    }
}
