package com.github.HpBtw.ms_pagamentos.service;

import com.github.HpBtw.ms_pagamentos.client.PedidoClient;
import com.github.HpBtw.ms_pagamentos.dto.PagamentoDTO;
import com.github.HpBtw.ms_pagamentos.entities.Pagamento;
import com.github.HpBtw.ms_pagamentos.entities.Status;
import com.github.HpBtw.ms_pagamentos.exceptions.PagamentoAprovadoException;
import com.github.HpBtw.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.HpBtw.ms_pagamentos.repositories.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository repo;

    @Autowired
    private PedidoClient pedidoClient;

    @Transactional
    public PagamentoDTO confirmarPagamentoDoPedido(Long id) {
        Pagamento p = repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pagamento de ID: " + id + " não encontrado!")
        );
        p.setStatus(Status.APROVADO);
        repo.save(p);
        pedidoClient.confirmarPagamento(p.getPedidoId());
        return new PagamentoDTO(p);
    }

    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAllPagamentos() {
        return repo.findAll().stream().map(PagamentoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PagamentoDTO findPagamentoById(Long id) {
        return new PagamentoDTO(repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pagamento de ID: " + id + " não encontrado")
        ));
    }

    @Transactional
    public PagamentoDTO savePagamento(PagamentoDTO dto) {
        Pagamento p = new Pagamento();
        mapDtoToPagamento(dto, p);
        p.setStatus(Status.CRIADO);
        return new PagamentoDTO(repo.save(p));
    }

    private void mapDtoToPagamento(PagamentoDTO dto, Pagamento p) {
        p.setNome(dto.getNome());
        p.setValor(dto.getValor());
        p.setValidade(dto.getValidade());
        p.setNumeroCartao(dto.getNumeroCartao());
        p.setCodigoSeguranca(dto.getCodigoSeguranca());
        p.setPedidoId(dto.getPedidoId());
    }

    @Transactional
    public PagamentoDTO updatePagamento(Long id, PagamentoDTO dto) {
        try {
            Pagamento p = repo.getReferenceById(id);

            if (p.getStatus().equals(Status.APROVADO)) {
                throw new PagamentoAprovadoException(String.format("Pagamento id %d já está APROVADO e não pode ser alterado.", id));
            }

            mapDtoToPagamento(dto, p);
            p.setStatus(dto.getStatus());
            return new PagamentoDTO(repo.save(p));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Pagamento de ID: " + id + " não encontrado");
        }
    }

    @Transactional
    public void deletePagamento(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Pagamento de ID: " + id + " não encontrado");

        repo.deleteById(id);
    }
}
