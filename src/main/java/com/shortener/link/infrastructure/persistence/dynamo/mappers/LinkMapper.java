package com.shortener.link.infrastructure.persistence.dynamo.mappers;

import com.shortener.link.domain.entities.Link;
import com.shortener.link.domain.value_objects.Guid;
import com.shortener.link.domain.value_objects.Url;
import com.shortener.link.infrastructure.persistence.dynamo.entities.LinkEntity;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper implements IMapper<Link, LinkEntity> {
    @Override
    public Link toEntity(LinkEntity linkEntity) throws IllegalArgumentException {
        if (linkEntity == null) {
            throw new IllegalArgumentException("Entidade de persistência Link deve ser informada antes de mapear");
        }

        return new Link(
                new Guid(linkEntity.getId()),
                new Url(linkEntity.getOriginalUrl()),
                new Url("https://mock.com.br"),
                linkEntity.getShortHash(),
                linkEntity.getCreatedAt()
        );
    }

    @Override
    public LinkEntity toPersistenceEntity(Link link) throws IllegalArgumentException {
        if (link == null) {
            throw new IllegalArgumentException("Entidade de domínio Link deve ser informada antes de mapear");
        }

        return new LinkEntity(
                link.id.getValue(),
                link.originalUrl.toString(),
                link.shortHash,
                link.duration,
                link.createdDate
        );
    }
}
