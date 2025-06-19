package com.shortener.link.infrastructure.persistence.dynamo.repositories;

import com.shortener.link.application.repositories.IBaseRepository;
import com.shortener.link.domain.value_objects.Guid;
import com.shortener.link.infrastructure.persistence.dynamo.mappers.IMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
abstract class BaseRepository<Entity, PersistenceEntity> implements IBaseRepository<Entity> {
    protected final DynamoDbTable<PersistenceEntity> dynamoDbTable;
    protected final IMapper<Entity, PersistenceEntity> entityMapper;
    private final List<Map<String, AttributeValue>> evaluatedKeys = new ArrayList<>();

    public BaseRepository(DynamoDbEnhancedClient dynamoDbClient, IMapper<Entity, PersistenceEntity> entityMapper, Class<PersistenceEntity> persistenceEntity) {
        this.entityMapper = entityMapper;
        this.dynamoDbTable = dynamoDbClient.table(
                persistenceEntity.getSimpleName().toLowerCase(),
                TableSchema.fromBean(persistenceEntity)
        );
    }

    @Override
    public Entity save(Entity entity) {
        this.dynamoDbTable.putItem(this.entityMapper.toPersistenceEntity(entity));
        return entity;
    }

    @Override
    public void delete(Entity entity) {
        PersistenceEntity persistenceEntity = this.entityMapper.toPersistenceEntity(entity);
        this.dynamoDbTable.deleteItem(persistenceEntity);
    }

    @Override
    public Entity findById(Guid id) {
        PersistenceEntity entity = this.dynamoDbTable.getItem(
                Key.builder().partitionValue(id.toString()).build()
        );

        if (entity == null) {
            return null;
        }

        return this.entityMapper.toEntity(entity);
    }

    @Override
    public List<Entity> findPaginated(int page, int size) {
        if (page < 1) throw new IllegalArgumentException("Página deve ser maior que 0");

        boolean hasEvaluatedKeys = !evaluatedKeys.isEmpty();
        if (hasEvaluatedKeys) {
            Map<String, AttributeValue> lastEvaluatedKey = evaluatedKeys.get(page - 1);
           PageIterable<PersistenceEntity> pages = this.dynamoDbTable.scan(
                   ScanEnhancedRequest.builder()
                           .exclusiveStartKey(lastEvaluatedKey)
                           .limit(size).build()
           );
           List<PersistenceEntity> items = pages.items().stream().toList();
           return items.stream().map(this.entityMapper::toEntity).toList();
        }

        ScanEnhancedRequest.Builder scanExpression = ScanEnhancedRequest.builder().limit(size);
        PageIterable<PersistenceEntity> pages = this.dynamoDbTable.scan(scanExpression.build());
        List<PersistenceEntity> items = new ArrayList<>();

        int currentPage = 0;
        for (Page<PersistenceEntity> pageData : pages) {
            currentPage++;
            evaluatedKeys.add(pageData.lastEvaluatedKey());

            if (currentPage == page) {
                items.addAll(pageData.items());
            }
        }

        return items.stream().map(this.entityMapper::toEntity).toList();
    }

    @Override
    public List<Entity> findAll() {
        SdkIterable<PersistenceEntity> entities = this.dynamoDbTable.scan().items();
        return entities.stream().map(this.entityMapper::toEntity).toList();
    }
}
