package com.shortener.link.domain.entities;

import com.shortener.link.domain.value_objects.Url;

import java.security.MessageDigest;

public class Link {
    public final Url originalUrl;
    private final String shortHash;
    private final Url baseUrl;

    public Link(Url originalUrl, Url baseUrl, String shortHash) {
        this.originalUrl = originalUrl;
        this.baseUrl = baseUrl;
        this.shortHash = shortHash;
    }

    public static Link shorten(Url originalUrl, Url baseUrl) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] urlBytes = messageDigest.digest(originalUrl.toString().getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte urlByte : urlBytes) hexString.append(String.format("%02x", urlByte));

            String hash = hexString.substring(0, 8);
            return new Link(originalUrl, baseUrl, hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Url getShortUrl() {
        return new Url(baseUrl.toString() + "/" + shortHash);
    }
}
