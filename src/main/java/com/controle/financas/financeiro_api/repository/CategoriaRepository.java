package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.controle.financas.financeiro_api.model.Categoria;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // 🌟 NOVO: Filtra as categorias pelo tipo (DESPESA ou RECEITA)
    List<Categoria> findByTipo(String tipo);
    
    // Retorna apenas as categorias principais (onde a categoria pai é nula)
    List<Categoria> findByCategoriaPaiIsNull();
    
    // Retorna as subcategorias pertencentes a uma categoria pai específica
    List<Categoria> findByCategoriaPaiId(Long categoriaPaiId);
}
