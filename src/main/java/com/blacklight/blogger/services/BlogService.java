package com.blacklight.blogger.services;

import com.blacklight.blogger.responses.BlogResponse;
import com.blacklight.blogger.entities.Blog;
import com.blacklight.blogger.dao.BlogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {

    @Autowired
    public BlogDAO blogDAO;

    public Blog getBlogById(Long id) {
        return blogDAO.getById(id);
    }

    public List<Blog> getAllBlogs() {
        return blogDAO.getAll();
    }

    public String getBlogTitleById(Long id) {
        return getBlogById(id).getTitle();
    }

    public Blog getBlogByTitle(String title) {
        return blogDAO.getBlogByTitle(title);
    }
    public List<Blog> getBlogsByAuthorName(String authorName) {
        return blogDAO.getBlogsByAuthorName(authorName);
    }

    public BlogResponse saveBlog(Blog blog) {
        if(titleExists(blog.getTitle())) {
            return BlogResponse.TITLE_ALREADY_EXISTS;
        }

        if(blog.getTitle().length() >= 50) {
            return BlogResponse.TITLE_TOO_LONG;
        }

        if(blog.getTitle().length() <= 10) {
            return BlogResponse.TITLE_TOO_SHORT;
        }

        blogDAO.save(blog);
        return BlogResponse.OK;
    }

    public int allBlogsSize() {
        return getAllBlogs().size();
    }

    boolean titleExists(String title) {
        if(blogDAO.getBlogByTitle(title) == null) {
            return false;
        }
        return true;
    }
}
