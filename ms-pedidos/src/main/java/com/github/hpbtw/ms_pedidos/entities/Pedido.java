package com.github.hpbtw.ms_pedidos.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tb_pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private Status status;
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "pedido",
        cascade = CascadeType.ALL, orphanRemoval = true
    ,fetch = FetchType.EAGER)
    private List<ItemDoPedido> itens = new ArrayList<>();

    public void calcularValorTotalDoPedido() {
        this.valorTotal = this.itens.stream()
                .map(item -> item.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
