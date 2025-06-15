package com.shortener.link.application.repositories;

import com.shortener.link.domain.value_objects.Guid;

public interface IBaseRepository<Entity> {
    Entity save(Entity entity);
    void delete(Entity entity);
    Entity findById(Guid id);
    Iterable<Entity> findPaginated(int page, int size);
    Iterable<Entity> findAll();
}
