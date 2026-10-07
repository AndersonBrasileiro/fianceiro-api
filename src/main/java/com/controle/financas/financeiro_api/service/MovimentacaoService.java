package com.controle.financas.financeiro_api.service;

import org.springframework.stereotype.Service;
import com.controle.financas.financeiro_api.model.Movimentacao;
import com.controle.financas.financeiro_api.repository.MovimentacaoRepository;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /**
     * REGRA DE NEGÓCIO: Retorna o extrato de uma conta específica, 
     * trazendo as movimentações ordenadas da mais recente para a mais antiga.
     */
    public List<Movimentacao> obterExtratoPorConta(Long contaId) {
        return movimentacaoRepository.findByContaIdOrderByDataHoraDesc(contaId);
    }
}
