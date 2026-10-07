package com.controle.financas.financeiro_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.service.ContaService;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping
    public ResponseEntity<List<Conta>> buscarTodas() {
        return ResponseEntity.ok(contaService.listarTodas());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Conta>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(contaService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Conta> criar(@RequestBody Conta conta) {
        Conta novaConta = contaService.salvar(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }
}
