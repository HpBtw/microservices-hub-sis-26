package com.github.hpbtw.ms_pedidos.service;

import com.github.hpbtw.ms_pedidos.dto.PedidoDTO;
import com.github.hpbtw.ms_pedidos.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository repo;

    public List<PedidoDTO> findAllPedidos() {
        return repo.findAll().stream().map(PedidoDTO::new).toList();
    }

}
