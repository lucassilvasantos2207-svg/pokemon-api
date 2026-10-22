package com.lucas.pokeapi.repository;

import com.lucas.pokeapi.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

    Optional<Pokemon> findByNomeIgnoreCase(String nome);

    Page<Pokemon> findByTiposContaining(String tipo, Pageable pageable);
}