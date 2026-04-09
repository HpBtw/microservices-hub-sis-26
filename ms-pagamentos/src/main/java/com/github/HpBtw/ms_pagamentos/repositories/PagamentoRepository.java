package com.github.HpBtw.ms_pagamentos.repositories;

import com.github.HpBtw.ms_pagamentos.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> { }