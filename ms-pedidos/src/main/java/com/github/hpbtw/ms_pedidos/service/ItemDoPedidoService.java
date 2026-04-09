package com.github.hpbtw.ms_pedidos.service;

import com.github.hpbtw.ms_pedidos.repositories.ItemDoPedidoRepository;
import com.github.hpbtw.ms_pedidos.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDoPedidoService {
    @Autowired
    private PedidoRepository pedidoRepo;
    @Autowired
    private ItemDoPedidoRepository repo;
}
