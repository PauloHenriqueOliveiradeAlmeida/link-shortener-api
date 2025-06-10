package com.shortener.link.domain.value_objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UrlTest {

    @Test
    public void testCreateUrlCorrectly() {
        Url url = new Url("https://mock.com");

        Assertions.assertEquals("https://mock.com", url.toString());
    }

    @Test
    public void testCreateUrlInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Url(""));
    }

    @Test
    public void testCreateUrlNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Url(null));
    }

    @Test
    public void testCreateUrlEquals() {
        Url url = new Url("https://mock.com");
        Assertions.assertEquals(new Url("https://mock.com"), url);
    }
}
