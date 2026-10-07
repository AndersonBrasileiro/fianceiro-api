package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.CentroCusto;
import java.util.List;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Long> {
    
    // REGRA DE NEGÓCIO DA USABILIDADE: Filtra por Usuário E pelo Tipo (DESPESA, RECEITA ou AMBOS)
    List<CentroCusto> findByUsuarioIdAndTipoIn(Long usuarioId, List<String> tipos);
}