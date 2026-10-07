package com.controle.financas.financeiro_api.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import com.controle.financas.financeiro_api.dto.DashboardDTO;
import com.controle.financas.financeiro_api.repository.LancamentoRepository;

@Service
public class DashboardService {

    private final LancamentoRepository lancamentoRepository;

    public DashboardService(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    /**
     * REGRA DE NEGÓCIO: Consolida os saldos futuros em aberto do usuário
     */
    public DashboardDTO obterResumoFinanceiro(Long usuarioId) {
        // 1. Busca a soma das receitas em aberto (pago = false)
        BigDecimal receber = lancamentoRepository.somarValoresEmAberto(usuarioId, "RECEITA");
        
        // 2. Busca a soma das despesas em aberto (pago = false)
        BigDecimal pagar = lancamentoRepository.somarValoresEmAberto(usuarioId, "DESPESA");

        // 3. Calcula o saldo projetado futuro
        BigDecimal projetado = receber.subtract(pagar);

        return new DashboardDTO(receber, pagar, projetado);
    }
}
