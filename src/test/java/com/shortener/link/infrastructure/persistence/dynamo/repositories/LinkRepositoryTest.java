package com.shortener.link.infrastructure.persistence.dynamo.repositories;

import com.shortener.link.domain.entities.Link;
import com.shortener.link.domain.enums.Uf;
import com.shortener.link.domain.value_objects.Ip;
import com.shortener.link.domain.value_objects.Location;
import com.shortener.link.domain.value_objects.Url;
import com.shortener.link.infrastructure.config.LocalStackConfig;
import com.shortener.link.infrastructure.persistence.dynamo.config.DynamoConfig;
import com.shortener.link.infrastructure.persistence.dynamo.entities.LinkEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;

@SpringBootTest
@Testcontainers
@Import({LocalStackConfig.class, DynamoConfig.class})
class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @BeforeEach
    void createTable() {
        String tableName = LinkEntity.class.getSimpleName().toLowerCase();
        TableSchema<LinkEntity> tableSchema = TableSchema.fromBean(LinkEntity.class);

        DynamoDbTable<LinkEntity> table = enhancedClient.table(tableName, tableSchema);
        try {
            table.deleteTable();
        } catch (Exception ignored) {}

        table.createTable();
    }

    @Test
    void testCreateLink() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        Link savedLink = this.linkRepository.save(link);

        Assertions.assertEquals(link, savedLink);
    }

    @Test
    void testFindLinkById() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link);
        Link foundLink = this.linkRepository.findById(link.id);

        Assertions.assertEquals(link, foundLink);
    }

    @Test
    void testFindLinkByShortHash() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link);
        Link foundLink = this.linkRepository.findByShortHash(link.getShortHash());

        Assertions.assertEquals(link, foundLink);
    }

    @Test
    void testNotFoundLinkByShortHash() {
        Assertions.assertNull(this.linkRepository.findByShortHash("short"));
    }

    @Test
    void testDeleteLink() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link);
        this.linkRepository.delete(link);

        Assertions.assertNull(this.linkRepository.findById(link.id));
    }

    @Test
    void testUpdateLink() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link);

        link.addNewCanAccess(new Ip("64.97.39.220", new Location("Cidade", Uf.AC)));
        Link updatedLink = this.linkRepository.save(link);

        Assertions.assertEquals(link, updatedLink);
    }

    @Test
    void testFindAllLinks() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link);
        Link link2 = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link2);
        Link link3 = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link3);
        Link link4 = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        this.linkRepository.save(link4);

        List<Link> links = this.linkRepository.findAll();

        Assertions.assertEquals(4, links.size());
    }
}