package com.github.HpBtw.ms_pagamentos.dto;

import com.github.HpBtw.ms_pagamentos.entities.Pagamento;
import com.github.HpBtw.ms_pagamentos.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagamentoDTO {
    private Long id;

    @NotNull(message = "O campo valor é obrigatório")
    @Positive(message = "O valor do pagamento deve ser um número positivo")
    private BigDecimal valor;

    @NotBlank(message = "O campo nome é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve entre 3 e 50 caracteres")
    private String nome;         // nome no cartao

    @NotBlank(message = "O campo número do cartão é obrigatório")
    @Size(min = 16, max = 16, message = "Número do cartão deve ter 16 caracteres")
    private String numeroCartao;   // XXXX XXXX XXXX XXXX

    @NotBlank(message = "O campo validade é obrigatório")
    @Size(min = 5, max = 5, message = "Validade do cartão deve ter 5 caracteres")
    private String validade;       // MM/AA

    @NotBlank(message = "O código de segurança é obrigatório")
    @Size(min = 3, max = 3, message = "Código de segurança deve ter 3 caracteres")
    private String codigoSeguranca; // xxx

    private Status status;

    @NotNull(message = "O campo ID do pedido é obrigatório")
    private Long pedidoId;

    public PagamentoDTO(Pagamento p) {
        id = p.getId();
        valor = p.getValor();
        nome = p.getNome();
        numeroCartao = p.getNumeroCartao();
        validade = p.getValidade();
        codigoSeguranca = p.getCodigoSeguranca();
        status = p.getStatus();
        pedidoId = p.getPedidoId();
    }
}
