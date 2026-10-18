package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.controle.financas.financeiro_api.model.Moeda;
import java.util.Optional;
import java.util.List;

public interface MoedaRepository extends JpaRepository<Moeda, Long> {
    
    // 🌟 FILTRO ATUALIZADO: Busca a moeda pelo código ISO, mas restringindo ao usuário logado
    Optional<Moeda> findByCodigoIsoAndUsuarioId(String codigoIso, Long usuarioId);

    // 🌟 NOVO: Lista todas as moedas cadastradas pertencentes apenas ao usuário logado
    List<Moeda> findByUsuarioId(Long usuarioId);
}
