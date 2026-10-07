package com.controle.financas.financeiro_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.controle.financas.financeiro_api.model.Categoria;
import com.controle.financas.financeiro_api.service.CategoriaService;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService javaCategoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.javaCategoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> buscarTodas() {
        return ResponseEntity.ok(javaCategoriaService.listarTodas());
    }

    @GetMapping("/principais")
    public ResponseEntity<List<Categoria>> buscarPrincipais() {
        return ResponseEntity.ok(javaCategoriaService.listarCategoriasPrincipais());
    }

    @GetMapping("/pai/{paiId}/subcategorias")
    public ResponseEntity<List<Categoria>> buscarSubcategorias(@PathVariable Long paiId) {
        return ResponseEntity.ok(javaCategoriaService.listarSubcategorias(paiId));
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {
        Categoria novaCategoria = javaCategoriaService.salvar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }
}
