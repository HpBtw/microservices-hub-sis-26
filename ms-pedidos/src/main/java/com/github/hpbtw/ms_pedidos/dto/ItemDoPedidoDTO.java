package com.github.hpbtw.ms_pedidos.dto;

import com.github.hpbtw.ms_pedidos.entities.ItemDoPedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemDoPedidoDTO {
    private Long id;

    @NotNull(message = "Campo quantidade é requerido")
    @Positive(message = "Campo quantidade deve ser maior que 0")
    private Integer quantidade;

    @NotBlank(message = "Campo descrição é requerido")
    private String descricao;

    @NotNull(message = "Campo preço unitário é requerido")
    @Positive(message = "Campo preço unitário é requerido")
    private BigDecimal precoUnitario;

    public ItemDoPedidoDTO(ItemDoPedido i) {
        id = i.getId();
        quantidade = i.getQuantidade();
        descricao = i.getDescricao();
        precoUnitario = i.getPrecoUnitario();
    }
}
