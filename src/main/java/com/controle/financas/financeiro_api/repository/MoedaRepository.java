package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.Moeda;
import java.util.Optional;

//@Repository
public interface MoedaRepository extends JpaRepository<Moeda, Long> {
    
    // Busca inteligente para encontrar a moeda pelo código de 3 letras (Ex: BRL, USD)
    Optional<Moeda> findByCodigoIso(String codigoIso);
}