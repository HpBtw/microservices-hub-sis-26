package com.github.hpbtw.ms_pedidos.controller;

import com.github.hpbtw.ms_pedidos.dto.PedidoDTO;
import com.github.hpbtw.ms_pedidos.entities.Pedido;
import com.github.hpbtw.ms_pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService service;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
        return ResponseEntity.ok(service.findAllPedidos());
    }
}
