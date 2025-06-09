package com.shortener.link.domain.entities;


import com.shortener.link.domain.value_objects.Url;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LinkTest {
    @Test
    public void testCreateLinkCorrectly() {
        Link link = new Link(new Url("https://mock.com"), new Url("https://mock.com"), "short");
        Assertions.assertEquals(new Url("https://mock.com"), link.originalUrl);
        Assertions.assertEquals(new Url("https://mock.com/short"), link.getShortUrl());
    }

    @Test
    public void testGenerateShortLinkCorrectly() {
        Link link = Link.shorten(new Url("https://mock.com/originalUrl"), new Url("https://mock.com"));
        Assertions.assertNotEquals(link.originalUrl, link.getShortUrl());
        Assertions.assertTrue(link.getShortUrl().toString().startsWith("https://mock.com/"));
        Assertions.assertTrue(link.getShortUrl().toString().length() < link.originalUrl.toString().length());
    }
}
