package com.shortener.link.domain.entities;


import com.shortener.link.domain.enums.Uf;
import com.shortener.link.domain.value_objects.Guid;
import com.shortener.link.domain.value_objects.Ip;
import com.shortener.link.domain.value_objects.Location;
import com.shortener.link.domain.value_objects.Url;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;

@SpringBootTest
public class LinkTest {
    @Test
    public void testCreateLinkCorrectly() {
        Link link = new Link(Guid.create(), new Url("https://mock.com"), new Url("https://mock.com"), "short", new Date());
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
        Link link = new Link(Guid.create(), new Url("https://mock.com"), new Url("https://mock.com"), "short", new Date(new Date().getTime() - duration - 1), duration);
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

    @Test
    public void testIncrementAccessCountOnPrivateLink() {
        Ip ip = new Ip("64.97.39.220", new Location("Cidade", Uf.AC));
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        link.addNewAccessed(ip);
        link.addNewAccessed(ip);
        Assertions.assertEquals(1, link.getAccessCount());
        Assertions.assertEquals(ip, link.getIpsThatAccessed().stream().toList().getFirst());
    }

    @Test
    public void testIncrementAccessCountOnPublicLink() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        link.addNewAccessed(new Ip("64.97.39.220", new Location("Cidade", Uf.AC)));
        Assertions.assertEquals(1, link.getAccessCount());
    Assertions.assertEquals(new Ip("64.97.39.220", new Location("Cidade", Uf.AC)), link.getIpsThatAccessed().stream().toList().getFirst());
    }

    @Test
    public void testAddAccessedInvalidOnPrivateLink() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        link.addNewCanAccess(new Ip("64.97.39.321", new Location("Cidade", Uf.AC)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> link.addNewAccessed(new Ip("64.97.39.220", new Location("Cidade", Uf.AC))));

    }

    @Test
    public void testLinkWithZeroAccessCount() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        Assertions.assertEquals(0, link.getAccessCount());
    }

    @Test
    public void testPublicLink() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"));
        Assertions.assertTrue(link.isPublic());
    }

    @Test
    public void testPrivateLink() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"), 60 * 60 * 1000);
        link.addNewCanAccess(new Ip("64.97.39.220", new Location("Cidade", Uf. AC)));
        Assertions.assertFalse(link.isPublic());
    }

    @Test
    public void testGetIpsCanAccess() {
        Link link = Link.shorten(new Url("https://mock.com"), new Url("https://mock.com"), 60 * 60 * 1000);
        link.addNewCanAccess(new Ip("64.97.39.220", new Location("Cidade", Uf.AC)));
        Assertions.assertTrue(link.ipCanAccess(new Ip("64.97.39.220", new Location("Cidade", Uf.AC))));
    }

}
