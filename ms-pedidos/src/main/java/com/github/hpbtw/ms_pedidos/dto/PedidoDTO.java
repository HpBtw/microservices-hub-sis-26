package com.github.hpbtw.ms_pedidos.dto;

import com.github.hpbtw.ms_pedidos.entities.ItemDoPedido;
import com.github.hpbtw.ms_pedidos.entities.Pedido;
import com.github.hpbtw.ms_pedidos.entities.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PedidoDTO {
    private Long id;

    @NotBlank(message = "Campo nome é requerido")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 caracteres")
    private String nome;

    @NotBlank(message = "Campo CPF é requerido")
    @Size(min = 11, max = 11, message = "Campo CPF deve ter 11 caracteres")
    // @CPF(message = "Informe um CPF válido") // valida se é um cpf
    private String cpf;
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private Status status;
    private BigDecimal valorTotal;

    @NotEmpty(message = "Pedido deve conter pelo menos um item.")
    private List<@Valid ItemDoPedidoDTO> itens = new ArrayList<>();

    public PedidoDTO(Pedido p){
        id = p.getId();
        nome = p.getNome();
        cpf = p.getCpf();
        data = p.getData();
        status = p.getStatus();
        valorTotal = p.getValorTotal();

        for (ItemDoPedido i : p.getItens()) {
            ItemDoPedidoDTO itemDTO = new ItemDoPedidoDTO(i);
            itens.add(itemDTO);
        }
    }
}
