package com.blacklight.blogger.dao;

import com.blacklight.blogger.entities.Author;
import com.blacklight.blogger.entities.Blog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public class AuthorDAOImpl implements AuthorDAO {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public Author getAuthorByName(String name) {

        try {
            Query query = entityManager.createQuery("SELECT a FROM Author a WHERE a.name = :name");
            query.setParameter("name", name);
            return (Author) query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Author getById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public List<Author> getAll() {
        return null;
    }

    @Transactional
    @Override
    public void save(Author author) {
        entityManager.persist(author);
    }

    @Transactional
    @Override
    public void delete(Author author) {
        entityManager.remove(author);
    }
}
