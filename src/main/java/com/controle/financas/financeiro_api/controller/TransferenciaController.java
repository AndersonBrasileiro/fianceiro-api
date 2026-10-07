package com.controle.financas.financeiro_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.controle.financas.financeiro_api.model.Transferencia;
import com.controle.financas.financeiro_api.service.TransferenciaService;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    /**
     * ENDPOINT: Realiza uma transferência de valores entre duas contas.
     * Rota: POST http://localhost:8080/api/transferencias
     */
    @PostMapping
    public ResponseEntity<Transferencia> realizarTransferencia(@RequestBody Transferencia transferencia) {
        Transferencia novaTransferencia = transferenciaService.transferir(transferencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTransferencia);
    }
}
