package com.blacklight.blogger.dao;

import com.blacklight.blogger.entities.Blog;

import java.util.List;

public interface BlogDAO extends DAO<Blog>{
    Blog getBlogByTitle(String title);
    List<Blog> getBlogsByAuthorName(String author);
}
