package com.shortener.link.infrastructure.persistence.dynamo.repositories;

import com.shortener.link.application.repositories.ILinkRepository;
import com.shortener.link.domain.entities.Link;
import com.shortener.link.infrastructure.persistence.dynamo.entities.LinkEntity;
import com.shortener.link.infrastructure.persistence.dynamo.mappers.LinkMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
public class LinkRepository extends BaseRepository<Link, LinkEntity> implements ILinkRepository {
    public LinkRepository(DynamoDbEnhancedClient dynamoDbClient, LinkMapper entityMapper) {
        super(dynamoDbClient, entityMapper, LinkEntity.class);
    }

    @Override
    public Link findByShortUrl(String shortUrl) {
        LinkEntity entity = this.dynamoDbTable.query(
                QueryEnhancedRequest.builder()
                        .queryConditional(
                                QueryConditional.keyEqualTo(
                                        Key.builder().partitionValue(shortUrl).build()
                                )
                        )
                        .build()
        )
                .items()
                .stream()
                .toList()
                .getFirst();

        if (entity == null) {
            return null;
        }

        return this.entityMapper.toEntity(entity);
    }
}
