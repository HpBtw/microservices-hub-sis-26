package com.github.hpbtw.ms_pedidos.repositories;

import com.github.hpbtw.ms_pedidos.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
