package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.Favorecido;
import java.util.List;

@Repository
public interface FavorecidoRepository extends JpaRepository<Favorecido, Long> {
    
    // Lista os contatos/favorecidos por usuário
    List<Favorecido> findByUsuarioId(Long usuarioId);
}