package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.Transferencia;
import java.util.List;

//@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByUsuarioId(Long usuarioId);
}