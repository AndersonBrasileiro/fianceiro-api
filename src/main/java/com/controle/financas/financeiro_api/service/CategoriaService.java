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

    // 🌟 ATUALIZADO: Filtra a listagem geral trazendo apenas o plano de contas do usuário logado
    public List<Categoria> listarTodasPorUsuario(Long usuarioId) {
        return categoriaRepository.findByUsuarioId(usuarioId);
    }

    // 🌟 ATUALIZADO: Busca categorias por tipo isolando por usuário
    public List<Categoria> listarPorTipoEUsuario(String tipo, Long usuarioId) {
        return categoriaRepository.findByTipoAndUsuarioId(tipo.toUpperCase(), usuarioId);
    }

    // 🌟 ATUALIZADO: Busca categorias principais isolando por usuário
    public List<Categoria> listarCategoriasPrincipaisPorUsuario(Long usuarioId) {
        return categoriaRepository.findByCategoriaPaiIsNullAndUsuarioId(usuarioId);
    }

    // 🌟 ATUALIZADO: Busca subcategorias isolando por usuário
    public List<Categoria> listarSubcategoriasPorUsuario(Long paiId, Long usuarioId) {
        return categoriaRepository.findByCategoriaPaiIdAndUsuarioId(paiId, usuarioId);
    }

    @Transactional
    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
}
