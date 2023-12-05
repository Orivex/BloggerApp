package com.blacklight.blogger.dao;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    <T> T getById(Long id);
    List<T> getAll();
    void save(T t);
    void delete(T t);
}
