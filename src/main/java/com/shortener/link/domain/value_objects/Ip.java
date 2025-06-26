package com.shortener.link.domain.value_objects;

import java.net.InetAddress;

public record Ip(String value, Location location) {
    public Ip {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("IP deve ser informado");
        if (location == null) throw new IllegalArgumentException("Localização deve ser informada");

        try {
            InetAddress inetAddress = InetAddress.getByName(value);
            boolean isInvalidIp = inetAddress.isLoopbackAddress() ||
                    inetAddress.isLinkLocalAddress() ||
                    inetAddress.isSiteLocalAddress() ||
                    inetAddress.isMulticastAddress() ||
                    inetAddress.isAnyLocalAddress();
            if (isInvalidIp) throw new IllegalArgumentException("IP inválido");

        } catch (Exception e) {
            throw new IllegalArgumentException("IP inválido");
        }
    }
}
