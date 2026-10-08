package com.controle.financas.financeiro_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import com.controle.financas.financeiro_api.model.Categoria;
import java.util.List;

//@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Retorna apenas as categorias principais (onde a categoria pai é nula)
    List<Categoria> findByCategoriaPaiIsNull();
    
    // Retorna as subcategorias pertencentes a uma categoria pai específica
    List<Categoria> findByCategoriaPaiId(Long categoriaPaiId);
}