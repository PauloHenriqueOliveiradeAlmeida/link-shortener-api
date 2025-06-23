package com.shortener.link.domain.value_objects;

public record Ip(String value, Location location) {
    public Ip {
        if (value == null) throw new IllegalArgumentException("IP deve ser informado");
        if (value.isEmpty()) throw new IllegalArgumentException("IP nao pode ser vazio");
        if (!value.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))
            throw new IllegalArgumentException("IP inválido");
        if (value.equals("0.0.0.0")) throw new IllegalArgumentException("IP não pode ser 0.0.0.0");
        if (value.equals("127.0.0.1")) throw new IllegalArgumentException("IP não pode ser 127.0.0.1");
        if (location == null) throw new IllegalArgumentException("Localidade deve ser informada");
    }
}
