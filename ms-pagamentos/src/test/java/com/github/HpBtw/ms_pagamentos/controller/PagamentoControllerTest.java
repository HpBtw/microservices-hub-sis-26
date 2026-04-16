package com.github.HpBtw.ms_pagamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.HpBtw.ms_pagamentos.dto.PagamentoDTO;
import com.github.HpBtw.ms_pagamentos.entities.Pagamento;
import com.github.HpBtw.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.HpBtw.ms_pagamentos.service.PagamentoService;
import com.github.HpBtw.ms_pagamentos.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PagamentoService service;
    private Pagamento p;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;
        p = Factory.createPagamento();
    }

    @Test
    void findAllPagamentosShouldReturnListPagamentoDTO() throws Exception {
        PagamentoDTO dto = new PagamentoDTO(p);
        List<PagamentoDTO> list = List.of(dto);
        Mockito.when(service.findAllPagamentos()).thenReturn(list);

        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON) );
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].id").value(p.getId()));
        result.andExpect(jsonPath("$[0].valor").value(p.getValor().doubleValue()));

        Mockito.verify(service).findAllPagamentos();
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists() throws Exception {
        PagamentoDTO dto = new PagamentoDTO(p);
        Mockito.when(service.findPagamentoById(existingId)).thenReturn(dto);

        mockMvc.perform(get("/pagamentos/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.valor").value(p.getValor().doubleValue()))
                .andExpect(jsonPath("$.status").value(p.getStatus().name()))
                .andExpect(jsonPath("$.pedidoId").value(p.getPedidoId()));

        Mockito.verify(service).findPagamentoById(existingId);
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    void findPagamentoByIdShouldReturn404WhenIdDoesNotExist() throws Exception {
        Mockito.when(service.findPagamentoById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Recurso não encontrado. ID: " + nonExistingId));

        mockMvc.perform(get("/pagamentos/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

        Mockito.verify(service).findPagamentoById(nonExistingId);
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    void createPagamentoShouldReturn201WhenValid() throws Exception {
        PagamentoDTO requestDto = new PagamentoDTO(Factory.createPagamentoSemId());

        String jsonRequestBody = objectMapper.writeValueAsString(requestDto);
        PagamentoDTO responseDto = new PagamentoDTO(p);

        Mockito.when(service.savePagamento(any(PagamentoDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(p.getId()))
                .andExpect(jsonPath("$.status").value(p.getStatus().name()))
                .andExpect(jsonPath("$.valor").value(p.getValor().doubleValue()))
                .andExpect(jsonPath("$.pedidoId").value(p.getPedidoId()));

        Mockito.verify(service).savePagamento(any(PagamentoDTO.class));
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    void createPagamentoShouldReturn422WhenInvalid() throws Exception {
        Pagamento pagamentoInvalido = Factory.createPagamentoSemId();
        pagamentoInvalido.setValor(BigDecimal.valueOf(0));
        pagamentoInvalido.setNome(null);
        PagamentoDTO requestDTO = new PagamentoDTO(pagamentoInvalido);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);
        PagamentoDTO responseDTO = new PagamentoDTO(pagamentoInvalido);

        Mockito.when(service.savePagamento(any(PagamentoDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/pagamentos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        Mockito.verifyNoInteractions(service);
    }
}