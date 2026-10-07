package com.controle.financas.financeiro_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.controle.financas.financeiro_api.model.Lancamento;
import com.controle.financas.financeiro_api.service.LancamentoService;

@RestController // Diz ao Spring que esta classe vai expor endpoints do tipo REST (JSON)
@RequestMapping("/api/lancamentos") // Define a rota base para este controlador
public class LancamentoController {

    private final LancamentoService lancamentoService;

    // Injeção via construtor padrão de mercado
    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    /**
     * ENDPOINT: Salva um novo lançamento no sistema.
     * Rota: POST http://localhost:8080/api/lancamentos
     */
    @PostMapping
    public ResponseEntity<Lancamento> criar(@RequestBody Lancamento lancamento) {
        Lancamento novoLancamento = lancamentoService.salvar(lancamento);
        // Retorna o objeto salvo com o status HTTP 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLancamento);
    }

     /**
     * ENDPOINT: Efetua a baixa/liquidação de um lançamento em aberto.
     * Rota: PUT http://localhost:8080/api/lancamentos/{id}/baixar
     */
    @org.springframework.web.bind.annotation.PutMapping("/{id}/baixar")
    public ResponseEntity<Lancamento> baixar(@org.springframework.web.bind.annotation.PathVariable Long id) {
        Lancamento lancamentoBaixado = lancamentoService.baixarLancamento(id);
        return ResponseEntity.ok(lancamentoBaixado);
    }
}