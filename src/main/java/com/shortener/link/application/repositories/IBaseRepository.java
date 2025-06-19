package com.shortener.link.application.repositories;

import com.shortener.link.domain.value_objects.Guid;
import java.util.List;

public interface IBaseRepository<Entity> {
    Entity save(Entity entity);
    void delete(Entity entity);
    Entity findById(Guid id);
    List<Entity> findAll();
}
