package com.github.hpbtw.ms_pedidos.service;

import com.github.hpbtw.ms_pedidos.dto.ItemDoPedidoDTO;
import com.github.hpbtw.ms_pedidos.dto.PedidoDTO;
import com.github.hpbtw.ms_pedidos.entities.ItemDoPedido;
import com.github.hpbtw.ms_pedidos.entities.Pedido;
import com.github.hpbtw.ms_pedidos.entities.Status;
import com.github.hpbtw.ms_pedidos.exceptions.PedidoPagoException;
import com.github.hpbtw.ms_pedidos.exceptions.ResourceNotFoundException;
import com.github.hpbtw.ms_pedidos.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository repo;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos() {
        return repo.findAll().stream().map(PedidoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO findPedidoById(Long id) {
        Pedido pedido =  repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pedido de ID: " + id + " não encontrado.")
        );
        return new PedidoDTO(pedido);
    }


    @Transactional
    public void confirmarPagamento (Long id) {
        Optional<Pedido> p = repo.findById(id);

        if (p.isEmpty()) throw new ResourceNotFoundException("Pedido de ID: " + id + " não encontrado.");

        p.get().setStatus(Status.PAGO);
        repo.save(p.get());
    }

    @Transactional(readOnly = true)
    public PedidoDTO savePedido(PedidoDTO dto) {
        Pedido p = new Pedido();
        p.setData(LocalDate.now());
        p.setStatus(Status.CRIADO);
        mapDtoToPedido(dto, p);
        p.calcularValorTotalDoPedido();
        return new PedidoDTO(repo.save(p));
    }

    @Transactional
    public PedidoDTO updatePedido(Long id, PedidoDTO dto) {
        try {
            Pedido p = repo.getReferenceById(id);

            if (p.getStatus().equals(Status.PAGO)) {
                throw new PedidoPagoException(String.format("Pedido de ID: %d já está PAGO e não pode ser alterado.", id));
            }

            p.getItens().clear();
            p.setData(LocalDate.now());
            // p.setStatus(Status.CRIADO); alterado depois do if adicionado acima
            mapDtoToPedido(dto, p);
            return new PedidoDTO(repo.save(p));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Pedido de ID: " + id + " não encontrado");
        }
    }
    public void deletePedidoById(Long id) {
        if(!repo.existsById(id)) throw new ResourceNotFoundException("Pedido de ID: " + id + " não encontrado.");

        repo.deleteById(id);
    }

    private void mapDtoToPedido(PedidoDTO dto, Pedido p) {
        p.setNome(dto.getNome());
        p.setCpf(dto.getCpf());

        for (ItemDoPedidoDTO itemDto : dto.getItens()) {
            ItemDoPedido item = new ItemDoPedido();
            item.setQuantidade(itemDto.getQuantidade());
            item.setDescricao(itemDto.getDescricao());
            item.setPrecoUnitario(itemDto.getPrecoUnitario());
            item.setPedido(p);
            p.getItens().add(item);
        }
    }
}
