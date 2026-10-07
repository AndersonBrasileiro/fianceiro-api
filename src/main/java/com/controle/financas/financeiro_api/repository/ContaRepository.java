package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.Conta;
import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    // Retorna apenas as contas bancárias pertencentes a um usuário específico
    List<Conta> findByUsuarioId(Long usuarioId);
}