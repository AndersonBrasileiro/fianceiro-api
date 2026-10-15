package com.controle.financas.financeiro_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.controle.financas.financeiro_api.model.Categoria;
import com.controle.financas.financeiro_api.service.CategoriaService;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // 🌟 ATUALIZADO: Agora aceita filtrar na URL ?tipo=DESPESA ou ?tipo=RECEITA
    @GetMapping
    public ResponseEntity<List<Categoria>> listar(@RequestParam(value = "tipo", required = false) String tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(categoriaService.listarPorTipo(tipo));
        }
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.salvar(categoria));
    }
}
