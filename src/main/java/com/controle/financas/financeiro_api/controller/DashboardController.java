package com.controle.financas.financeiro_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.dto.DashboardDTO;
import com.controle.financas.financeiro_api.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // 🌟 CORRIGIDO: Vinculado ao método real do seu Service (obterResumoFinanceiro)
    @GetMapping
    public ResponseEntity<DashboardDTO> obterResumoDashboard(Authentication authentication) {
        // Extrai o usuário logado de dentro do contexto de segurança do token JWT
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        
        // Chama o método exato que existe no seu DashboardService.java
        DashboardDTO resumo = dashboardService.obterResumoFinanceiro(usuarioLogado.getId());
        return ResponseEntity.ok(resumo);
    }
}
