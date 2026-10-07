package com.controle.financas.financeiro_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.controle.financas.financeiro_api.dto.DashboardDTO;
import com.controle.financas.financeiro_api.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * ENDPOINT: Retorna o resumo consolidado de títulos em aberto para o Dashboard.
     * Rota: GET http://localhost:8080/api/dashboard/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<DashboardDTO> carregarDashboard(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(dashboardService.obterResumoFinanceiro(usuarioId));
    }
}
