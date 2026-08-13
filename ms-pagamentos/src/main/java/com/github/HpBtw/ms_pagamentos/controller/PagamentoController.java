package com.github.HpBtw.ms_pagamentos.controller;

import com.github.HpBtw.ms_pagamentos.dto.PagamentoDTO;
import com.github.HpBtw.ms_pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> getAll() {
        List<PagamentoDTO> pagamentosDTO = service.findAllPagamentos();
        return ResponseEntity.ok(pagamentosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findPagamentoById(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> createPagamento(@RequestBody @Valid PagamentoDTO dto) {
        dto = service.savePagamento(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<PagamentoDTO> confirmarPagamentoDoPedido(@PathVariable
                                                                   @NotNull Long id) {
        return ResponseEntity.ok(service.confirmarPagamento(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> updatePagamento(@PathVariable Long id, @RequestBody @Valid PagamentoDTO dto) {
        return ResponseEntity.ok(service.updatePagamento(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePagamento(@PathVariable Long id) {
        service.deletePagamento(id);
        return ResponseEntity.noContent().build();
    }
}
