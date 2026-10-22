package com.lucas.pokeapi.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Pokemon {

    @Id
    private Integer id;
    private String nome;
    private Integer altura;
    private Integer peso;
    private Integer hp;
    private Integer ataque;
    private Integer defesa;
    private Integer velocidade;
    private String sprite;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tipos = new ArrayList<>();
}