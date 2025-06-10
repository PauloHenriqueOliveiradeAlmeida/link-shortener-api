package com.shortener.link.domain.entities;


import com.shortener.link.domain.value_objects.Url;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class LinkTest {
    @Test
    public void testCreateLinkCorrectly() {
        Link link = new Link(new Url("https://mock.com"), new Url("https://mock.com"), "short", new Date());
        Assertions.assertEquals(new Url("https://mock.com"), link.originalUrl);
        Assertions.assertEquals(new Url("https://mock.com/short"), link.getShortUrl());
    }

    @Test
    public void testGenerateShortLinkCorrectly() {
        Link link = Link.shorten(new Url("https://mock.com/originalUrl"), new Url("https://mock.com"), 60 * 60 * 1000);
        Assertions.assertNotEquals(link.originalUrl, link.getShortUrl());
        Assertions.assertTrue(link.getShortUrl().toString().startsWith("https://mock.com/"));
        Assertions.assertTrue(link.getShortUrl().toString().length() < link.originalUrl.toString().length());
    }

    @Test
    public void testGenerateShortLinkInvalidDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"), 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"), 10 * 24 * 60 * 60 * 1000));
    }

    @Test
    public void testIsNotExpired() {
        int duration = 3 * 24 * 60 * 60 * 1000;
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"), duration);        Assertions.assertFalse(link.isExpired());
    }


    @Test
    public void testIsExpired() {
        int duration = 3 * 24 * 60 * 60 * 1000;
        Link link = new Link(new Url("https://mock.com"), new Url("https://mock.com"), "short", new Date(new Date().getTime() - duration - 1), duration);
        Assertions.assertTrue(link.isExpired());
    }

    @Test
    public void testIsNotExpiredWhenDurationIsNull() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        Assertions.assertFalse(link.isExpired());
    }

    @Test
    public void testGetShortUrl() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        Assertions.assertTrue(link.getShortUrl().toString().startsWith("https://mock.com/"));
    }
}
