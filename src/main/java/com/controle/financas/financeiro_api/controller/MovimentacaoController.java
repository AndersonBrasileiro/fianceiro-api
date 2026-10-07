package com.controle.financas.financeiro_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.controle.financas.financeiro_api.model.Movimentacao;
import com.controle.financas.financeiro_api.service.MovimentacaoService;
import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    /**
     * ENDPOINT: Retorna o extrato detalhado de uma conta específica.
     * Rota: GET http://localhost:8080/api/movimentacoes/conta/{contaId}
     */
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<Movimentacao>> buscarExtratoPorConta(@PathVariable Long contaId) {
        List<Movimentacao> extrato = movimentacaoService.obterExtratoPorConta(contaId);
        return ResponseEntity.ok(extrato);
    }
}
