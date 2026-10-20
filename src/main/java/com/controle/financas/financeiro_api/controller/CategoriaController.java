package com.controle.financas.financeiro_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.controle.financas.financeiro_api.model.Categoria;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.service.CategoriaService;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // 🌟 ATUALIZADO COM AUTENTICAÇÃO DIRECTA: Filtra as buscas usando o ID do Token JWT
    @GetMapping
    public ResponseEntity<List<Categoria>> listar(
            @RequestParam(value = "tipo", required = false) String tipo,
            @AuthenticationPrincipal Usuario usuarioLogado) { // Injeta o usuário logado automaticamente!
        
        if (tipo != null) {
            return ResponseEntity.ok(categoriaService.listarPorTipoEUsuario(tipo, usuarioLogado.getId()));
        }
        return ResponseEntity.ok(categoriaService.listarTodasPorUsuario(usuarioLogado.getId()));
    }

    // 🌟 ATUALIZADO COM BLINDAGEM MULTI-TENANT: Amarra a categoria ao usuário logado antes de salvar
    @PostMapping
    public ResponseEntity<Categoria> criar(
            @RequestBody Categoria categoria,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        // Garante que o ID do dono da licença seja gravado de forma física no banco
        categoria.setUsuario(usuarioLogado);
        return ResponseEntity.ok(categoriaService.salvar(categoria));
    }
}
