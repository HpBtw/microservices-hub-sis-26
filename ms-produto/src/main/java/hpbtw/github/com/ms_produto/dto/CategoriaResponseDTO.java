package com.github.HpBtw.ms_pagamentos.dto;

import hpbtw.github.com.ms_produto.entities.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoriaResponseDTO {
    private Long id;
    private String nome;

    public CategoriaResponseDTO(Categoria c) {
        this.id = c.getId();
        this.nome = c.getNome();
    }
}
