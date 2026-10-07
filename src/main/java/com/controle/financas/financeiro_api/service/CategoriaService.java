package com.controle.financas.financeiro_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.model.Categoria;
import com.controle.financas.financeiro_api.repository.CategoriaRepository;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarCategoriasPrincipais() {
        return categoriaRepository.findByCategoriaPaiIsNull();
    }

    public List<Categoria> listarSubcategorias(Long paiId) {
        return categoriaRepository.findByCategoriaPaiId(paiId);
    }

    @Transactional
    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
}
