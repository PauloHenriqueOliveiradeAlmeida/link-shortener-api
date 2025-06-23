package com.shortener.link.infrastructure.persistence.dynamo.repositories;

import com.shortener.link.application.repositories.ILinkRepository;
import com.shortener.link.domain.entities.Link;
import com.shortener.link.infrastructure.persistence.dynamo.entities.LinkEntity;
import com.shortener.link.infrastructure.persistence.dynamo.mappers.LinkMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@Repository
public class LinkRepository extends BaseRepository<Link, LinkEntity> implements ILinkRepository {
    public LinkRepository(DynamoDbEnhancedClient dynamoDbClient, LinkMapper entityMapper) {
        super(dynamoDbClient, entityMapper, LinkEntity.class);
    }

    @Override
    public Link findByShortHash(String shortHash) {
        List<LinkEntity> entity = this.dynamoDbTable
                .index("short-url-hash-index")
                .query(request -> request.queryConditional(
                        QueryConditional.keyEqualTo(
                        Key.builder().partitionValue(shortHash).build()
                )).limit(1))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();

        if (entity.isEmpty()) return null;

        return this.entityMapper.toEntity(entity.getFirst());
    }
}
