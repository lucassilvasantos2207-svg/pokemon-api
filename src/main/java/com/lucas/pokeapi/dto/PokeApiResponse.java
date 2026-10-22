package com.lucas.pokeapi.dto;

import java.util.List;

public record PokeApiResponse(
        Integer id,
        String name,
        Integer height,
        Integer weight,
        List<TypeSlot> types,
        List<StatSlot> stats,
        Sprites sprites) {

    public record TypeSlot(TypeInfo type) {}

    public record TypeInfo(String name) {}

    public record StatSlot(Integer base_stat, StatInfo stat) {}

    public record StatInfo(String name) {}

    public record Sprites(String front_default) {}
}