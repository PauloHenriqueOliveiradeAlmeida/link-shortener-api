package com.shortener.link.domain.value_objects;

import com.shortener.link.domain.enums.Uf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LocationTest {

    @Test
    public void testCreateLocationCorrectly() {
        Location location = new Location("Cidade", Uf.AC);
        Assertions.assertEquals("Cidade", location.city());
        Assertions.assertEquals(Uf.AC, location.uf());
    }

    @Test
    public void testCreateLocationInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Location(null, Uf.AC));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Location("", Uf.AC));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Location("Cidade", null));
    }
}
