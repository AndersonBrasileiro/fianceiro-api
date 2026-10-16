package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.controle.financas.financeiro_api.model.Categoria;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // 🌟 FILTROS ATUALIZADOS: Todos agora exigem o ID do usuário para isolar os dados
    
    List<Categoria> findByUsuarioId(Long usuarioId);
    
    List<Categoria> findByTipoAndUsuarioId(String tipo, Long usuarioId);
    
    List<Categoria> findByCategoriaPaiIsNullAndUsuarioId(Long usuarioId);
    
    List<Categoria> findByCategoriaPaiIdAndUsuarioId(Long categoriaPaiId, Long usuarioId);
}
