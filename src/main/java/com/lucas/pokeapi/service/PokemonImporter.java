package com.lucas.pokeapi.service;

import com.lucas.pokeapi.dto.PokeApiResponse;
import com.lucas.pokeapi.model.Pokemon;
import com.lucas.pokeapi.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PokemonImporter implements CommandLineRunner {

    private final PokemonRepository repository;
    private final RestClient client = RestClient.create("https://pokeapi.co/api/v2");

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return; // já importado

        for (int i = 1; i <= 151; i++) {
            PokeApiResponse r = client.get()
                    .uri("/pokemon/{id}", i)
                    .retrieve()
                    .body(PokeApiResponse.class);

            Pokemon p = new Pokemon();
            p.setId(r.id());
            p.setNome(r.name());
            p.setAltura(r.height());
            p.setPeso(r.weight());
            p.setSprite(r.sprites().front_default());
            p.setTipos(r.types().stream().map(t -> t.type().name()).toList());

            for (var s : r.stats()) {
                switch (s.stat().name()) {
                    case "hp" -> p.setHp(s.base_stat());
                    case "attack" -> p.setAtaque(s.base_stat());
                    case "defense" -> p.setDefesa(s.base_stat());
                    case "speed" -> p.setVelocidade(s.base_stat());
                }
            }
            repository.save(p);
        }
    }
}