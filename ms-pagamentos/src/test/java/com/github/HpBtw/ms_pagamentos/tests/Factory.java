package com.github.HpBtw.ms_pagamentos.tests;

import com.github.HpBtw.ms_pagamentos.entities.Pagamento;
import com.github.HpBtw.ms_pagamentos.entities.Status;

import java.math.BigDecimal;

public class Factory {
    public static Pagamento createPagamento() {
        Pagamento p = new Pagamento(1L, BigDecimal.valueOf(32.25), "Brienne de Tarth", "1234123412341234", "07/15", "354", Status.CRIADO, 1L);

        return p;
    }

    public static Pagamento createPagamentoSemId() {
        Pagamento p = createPagamento();
        p.setId(null);
        return p;
    }
}
