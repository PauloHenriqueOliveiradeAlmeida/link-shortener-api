package com.shortener.link.domain.value_objects;

import java.net.URI;
import java.net.URL;

public class Url {
    private final URL value;

    public Url(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Url deve ser informada");
        }

        if (value.isEmpty()) {
            throw new IllegalArgumentException("Url não pode ser vazia");
        }

        try {
           this.value = URI.create(value).toURL();
        } catch (Exception e) {
            throw new IllegalArgumentException("Url inválida");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Url) {
            return this.value.toString().equals(obj.toString());
        }
        return false;
    }
}
