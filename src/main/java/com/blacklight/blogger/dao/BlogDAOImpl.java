package com.blacklight.blogger.dao;

import com.blacklight.blogger.entities.Blog;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Repository
public class BlogDAOImpl implements BlogDAO {
    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public Blog getById(Long id) {
         return entityManager.find(Blog.class, id);
    }

    @Override
    public Blog getBlogByTitle(String title) {
        Query query = entityManager.createQuery("SELECT b FROM Blog b WHERE b.title = :title");
        query.setParameter("title", title);

        try{
            return (Blog) query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Blog> getBlogsByAuthorName (String authorName) {
        Query query = entityManager.createQuery("SELECT b FROM Blog b WHERE b.authorName = :authorName");
        query.setParameter("authorName", authorName);
        return query.getResultList();
    }

    @Override
    public List<Blog> getAll() {
        Query query = entityManager.createQuery("SELECT b FROM Blog b");
        return query.getResultList();
    }

    @Transactional
    @Override
    public void save(Blog blog) {
        entityManager.persist(blog);
    }

    @Transactional
    @Override
    public void delete(Blog blog) {
        entityManager.remove(blog);
    }
}
