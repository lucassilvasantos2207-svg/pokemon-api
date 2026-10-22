package com.lucas.pokeapi.controller;

import com.lucas.pokeapi.model.Pokemon;
import com.lucas.pokeapi.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemons")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonRepository repository;

    @GetMapping
    public Page<Pokemon> listar(@RequestParam(required = false) String tipo, Pageable pageable) {
        return tipo == null
                ? repository.findAll(pageable)
                : repository.findByTiposContaining(tipo.toLowerCase(), pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> buscarPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Pokemon> buscarPorNome(@PathVariable String nome) {
        return repository.findByNomeIgnoreCase(nome)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}