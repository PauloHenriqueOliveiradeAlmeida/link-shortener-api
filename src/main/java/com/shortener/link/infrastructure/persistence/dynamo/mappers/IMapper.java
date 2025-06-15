package com.shortener.link.infrastructure.persistence.dynamo.mappers;

public interface IMapper<Entity, PersistenceEntity> {
    Entity toEntity(PersistenceEntity persistenceEntity);
    PersistenceEntity toPersistenceEntity(Entity entity);
}
