package com.shortener.link.domain.value_objects;

import com.shortener.link.domain.enums.Uf;

public record Location(String city, Uf uf) {
    public Location {
        if (city == null) throw new IllegalArgumentException("Cidade deve ser informada");
        if (city.isEmpty()) throw new IllegalArgumentException("Cidade não pode ser vazia");
        if (uf == null) throw new IllegalArgumentException("UF deve ser informada");

    }
}
