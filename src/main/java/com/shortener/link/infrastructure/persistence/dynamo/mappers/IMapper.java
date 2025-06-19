package com.shortener.link.infrastructure.persistence.dynamo.mappers;

public interface IMapper<Entity, PersistenceEntity> {
    Entity toEntity(PersistenceEntity persistenceEntity) throws IllegalArgumentException;
    PersistenceEntity toPersistenceEntity(Entity entity) throws IllegalArgumentException;
}
