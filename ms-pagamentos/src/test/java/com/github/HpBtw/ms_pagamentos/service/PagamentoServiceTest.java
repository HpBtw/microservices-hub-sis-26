package com.github.HpBtw.ms_pagamentos.service;

import com.github.HpBtw.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.HpBtw.ms_pagamentos.repositories.PagamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repo;

    @InjectMocks
    private PagamentoService service;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;
    }

    @Test
    void deletePagamentoByIdShouldDeleteWhenIdExists() {
        Mockito.when(repo.existsById(existingId)).thenReturn(true);
        service.deletePagamento(existingId);

        Mockito.verify(repo).existsById(existingId);
        Mockito.verify(repo, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("deletePagamentoById deveria lançar ResourceNotFoundException quando o ID não existir")
    void deletePagamentoByIdShouldDeleteWhenIdDoestNotExists() {
        Mockito.when(repo.existsById(nonExistingId)).thenReturn(false); // arrange

        Assertions.assertThrows(ResourceNotFoundException.class, () -> { // act + assert
            service.deletePagamento(nonExistingId);
        });

        // behavior
        Mockito.verify(repo).existsById(nonExistingId);

        Mockito.verify(repo, Mockito.never()).deleteById(Mockito.anyLong());
    }
}
