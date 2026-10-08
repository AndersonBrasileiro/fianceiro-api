package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.Usuario;
import java.util.Optional;

//@Repository // Avisa ao Spring que esta é uma classe de persistência de dados
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // O Spring Data JPA cria a busca por email automaticamente baseado apenas no nome do método!
    Optional<Usuario> findByEmail(String email);
}