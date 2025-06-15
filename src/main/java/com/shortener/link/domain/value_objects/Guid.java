package com.shortener.link.domain.value_objects;

import java.util.UUID;

public class Guid {
    private final UUID value;

    public Guid(UUID value) {
        if (value == null) throw new IllegalArgumentException("Guid deve ser informado");

        this.value = value;
    }

    public static Guid fromString(String value) {
        if (value == null) throw new IllegalArgumentException("Guid deve ser informado");
        if (value.isEmpty()) throw new IllegalArgumentException("Guid não pode ser vazio");

        try {
            UUID uuid = UUID.fromString(value);
            return new Guid(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Guid inválido");
        }
    }

    public static Guid create() {
        return new Guid(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Guid || obj instanceof String || obj instanceof UUID) {
            return this.toString().equals(obj.toString());
        }
        return false;
    }
}
