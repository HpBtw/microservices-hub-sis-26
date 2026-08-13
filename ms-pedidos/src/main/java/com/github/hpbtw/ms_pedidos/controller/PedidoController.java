package com.github.hpbtw.ms_pedidos.controller;

import com.github.hpbtw.ms_pedidos.dto.PedidoDTO;
import com.github.hpbtw.ms_pedidos.entities.Pedido;
import com.github.hpbtw.ms_pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService service;

    @GetMapping("/port")
    public String port(@Value("${local.server.port}") String porta) {
        return "Instância respondeu na porta " + porta;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
        return ResponseEntity.ok(service.findAllPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> getPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findPedidoById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> createPedido(@RequestBody @Valid PedidoDTO dto) {
        dto = service.savePedido(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{pedidoId}/pagamento/confirmado")
    public void confirmarPagamento(@PathVariable Long id) { service.confirmarPagamento(id);}

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> updatePedido(@PathVariable Long id,
                                                  @Valid @RequestBody PedidoDTO dto) {
        return ResponseEntity.ok((service.updatePedido(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        service.deletePedidoById(id);

        return ResponseEntity.noContent().build();
    }
}
