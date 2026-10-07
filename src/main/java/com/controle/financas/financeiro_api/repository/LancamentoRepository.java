package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.Lancamento;
import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    
    // Busca lançamentos filtrando se estão pagos (true) ou em aberto (false) para o usuário
    List<Lancamento> findByUsuarioIdAndPago(Long usuarioId, boolean pago);
}