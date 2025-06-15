package com.shortener.link.domain.value_objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class GuidTest {

    @Test
    public void testCreateGuidCorrectly() {
        UUID uuid = UUID.randomUUID();
        Guid guid = new Guid(uuid);
        Assertions.assertEquals(uuid.toString(), guid.toString());
    }

    @Test
    public void testCreateGuidFromStringCorrectly() {
        UUID uuid = UUID.randomUUID();
        Guid guid = Guid.fromString(uuid.toString());
        Assertions.assertEquals(uuid.toString(), guid.toString());
    }

    @Test
    public void testGenerateGuidCorrectly() {
        Guid guid = Guid.create();
        Assertions.assertNotNull(guid.toString());
    }

    @Test
    public void testEqualsGuid() {
        UUID uuid = UUID.randomUUID();
        Guid guid = Guid.fromString(uuid.toString());
        Guid guid2 = Guid.fromString(uuid.toString());
        Assertions.assertEquals(guid, guid2);
    }

    @Test
    public void testCreateNullGuid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Guid(null));
    }

    @Test
    public void testCreateInvalidGuidFromString() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Guid.fromString(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Guid.fromString(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Guid.fromString("123"));
    }

    @Test
    public void testNotEqualsGuid() {
        Guid guid = Guid.create();
        Guid guid2 = Guid.create();
        Assertions.assertNotEquals(guid, guid2);
    }
}
