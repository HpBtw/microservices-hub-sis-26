package com.github.HpBtw.ms_pagamentos.service;

import com.github.HpBtw.ms_pagamentos.dto.PagamentoDTO;
import com.github.HpBtw.ms_pagamentos.entities.Pagamento;
import com.github.HpBtw.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.HpBtw.ms_pagamentos.repositories.PagamentoRepository;
import com.github.HpBtw.ms_pagamentos.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repo;

    @InjectMocks
    private PagamentoService service;

    private Long existingId;
    private Long nonExistingId;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;

        pagamento = Factory.createPagamento();
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

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists() {

        Mockito.when(repo.findById(existingId)).thenReturn(Optional.of(pagamento));

        PagamentoDTO result = service.findPagamentoById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());

        Mockito.verify(repo).findById(existingId);
        Mockito.verifyNoMoreInteractions(repo);
    }

    @Test
    void findPagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Mockito.when(repo.findById(nonExistingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.findPagamentoById(nonExistingId));

        Mockito.verify(repo).findById(nonExistingId);
        Mockito.verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("Dado parâmetros válidos e Id nulo " + "quando chamar Salvar Pagamento então deve gerar Id e persistir um Pagamento")
    void givenValidParamsAndIdIsNull_whenSave_thenShouldPersistPagamento() {
        Mockito.when(repo.save(any(Pagamento.class)))
                .thenReturn(pagamento);

        pagamento.setId(null);

        PagamentoDTO dto = new PagamentoDTO(pagamento);
        PagamentoDTO result = service.savePagamento(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());

        Mockito.verify(repo).save(any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(repo);
    }

    @Test
    void updatePagamentoShouldReturnPagamentoDTOWhenIdExists() {
        Long id = pagamento.getId();
        Mockito.when(repo.getReferenceById(id))
                .thenReturn(pagamento);
        Mockito.when(repo.save(any(Pagamento.class)))
                .thenReturn(pagamento);

        PagamentoDTO result = service.updatePagamento(id, new PagamentoDTO(pagamento));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());
        Mockito.verify(repo).getReferenceById(id);
        Mockito.verify(repo).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(repo);
    }

    @Test
    void updatePagamentoShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Mockito.when(repo.getReferenceById(nonExistingId))
                .thenThrow(EntityNotFoundException.class);
        PagamentoDTO dto = new PagamentoDTO(pagamento);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.updatePagamento(nonExistingId, dto));

        Mockito.verify(repo).getReferenceById(nonExistingId);
        Mockito.verify(repo, Mockito.never()).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(repo);
    }
}
