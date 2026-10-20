package com.controle.financas.financeiro_api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.controle.financas.financeiro_api.model.CentroCusto;
import com.controle.financas.financeiro_api.service.CentroCustoService;

@RestController
@RequestMapping("/api/centros-custo")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class CentroCustoController {

    private final CentroCustoService centroCustoService;

    public CentroCustoController(CentroCustoService centroCustoService) {
        this.centroCustoService = centroCustoService;
    }

    /**
     * ENDPOINT: Cadastra um novo Centro de Custo/Receita associado ao Token.
     */
    @PostMapping
    public ResponseEntity<CentroCusto> criar(@RequestBody CentroCusto centroCusto) {
        CentroCusto novoCentro = centroCustoService.salvar(centroCusto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCentro);
    }

    /**
     * ENDPOINT DA TELA: Retorna apenas os centros válidos para o tipo de tela aberta.
     * Rota: GET http://localhost:8080/api/centros-custo/filtrar?fluxo=DESPESA
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<CentroCusto>> buscarFiltrados(@RequestParam String fluxo) {
        List<CentroCusto> lista = centroCustoService.listarPorFluxoELogado(fluxo);
        return ResponseEntity.ok(lista);
    }
}
