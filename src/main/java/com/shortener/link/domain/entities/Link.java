package com.shortener.link.domain.entities;
import com.shortener.link.domain.value_objects.Guid;
import com.shortener.link.domain.value_objects.Ip;
import com.shortener.link.domain.value_objects.Url;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashSet;

public class Link {
    public final Guid id;
    public final Url originalUrl;
    public final String shortHash;
    private final Url baseUrl;
    public final Integer duration;
    public final Date createdDate;
    private final HashSet<Ip> ipsCanAccess = new HashSet<>();
    private final HashSet<Ip> ipsThatAccessed = new HashSet<>();

    private static final int MINUTE_IN_MILLISECONDS = 60 * 1000;
    private static final int THREE_DAYS_IN_MILLISECONDS = 3 * 24 * 60 * 60 * 1000;

    public Link(Guid id, Url originalUrl, Url baseUrl, String shortHash, Date createdDate, Integer duration) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.baseUrl = baseUrl;
        this.shortHash = shortHash;
        this.createdDate = createdDate;

        if (duration == null) {
            this.duration = null;
            return;
        }

        if (duration < MINUTE_IN_MILLISECONDS || duration > THREE_DAYS_IN_MILLISECONDS)
            throw new IllegalArgumentException("A duração deve estar entre 1 minuto e 3 dias");

        this.duration = duration;
    }

    public Link(Guid id, Url originalUrl, Url baseUrl, String shortHash, Date createdDate) {
        this(id, originalUrl, baseUrl, shortHash, createdDate, null);
    }

    public static Link shorten(Url originalUrl, Url baseUrl, Integer duration) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] urlBytes = messageDigest.digest(originalUrl.toString().getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte urlByte : urlBytes) hexString.append(String.format("%02x", urlByte));

            String hash = hexString.substring(0, 8);
            return new Link(Guid.create(), originalUrl, baseUrl, hash, new Date(), duration);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Link shorten(Url originalUrl, Url baseUrl) {
        return shorten(originalUrl, baseUrl, null);
    }

    public Url getShortUrl() {
        return new Url(baseUrl.toString() + "/" + shortHash);
    }

    public boolean isExpired() {
        if (duration == null) return false;
        Date expirationDate = new Date(createdDate.getTime() + duration);
        return expirationDate.before(new Date());
    }

    public void addNewAccessed(Ip ip) {
        if (!isPublic() && !ipCanAccess(ip)) throw new IllegalArgumentException("IP não pode acessar esse link");
        ipsThatAccessed.add(ip);
    }

    public HashSet<Ip> getIpsThatAccessed() {
        return new HashSet<>(ipsThatAccessed);
    }

    public int getAccessCount() {
        return ipsThatAccessed.size();
    }

    public void addNewCanAccess(Ip ip) {
        ipsCanAccess.add(ip);
    }

    public boolean ipCanAccess(Ip ip) {
        if (isPublic()) return true;
        return ipsCanAccess.stream().anyMatch(ip::equals);
    }

    public boolean isPublic() {
        return ipsCanAccess.isEmpty();
    }
}
