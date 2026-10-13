package com.controle.financas.financeiro_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.model.Movimentacao;
import com.controle.financas.financeiro_api.model.Transferencia;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.repository.ContaRepository;
import com.controle.financas.financeiro_api.repository.MovimentacaoRepository;
import com.controle.financas.financeiro_api.repository.TransferenciaRepository;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public TransferenciaService(TransferenciaRepository transferenciaRepository, 
                                ContaRepository contaRepository, 
                                MovimentacaoRepository movimentacaoRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /**
     * REGRA DE NEGÓCIO MULTI-TENANT: Efetua uma transferência entre contas blindando os saldos
     * e garantindo que o usuário só movimente contas que realmente pertencem a ele.
     */
    @Transactional
    public Transferencia transferir(Transferencia transferencia) {
        BigDecimal valor = transferencia.getValor();

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }

        org.springframework.security.core.Authentication auth = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();

        Conta origen = contaRepository.findById(transferencia.getContaOrigem().getId())
            .orElseThrow(() -> new IllegalArgumentException("Conta origem não encontrada."));
            
        Conta destino = contaRepository.findById(transferencia.getContaDestino().getId())
            .orElseThrow(() -> new IllegalArgumentException("Conta destino não encontrada."));

        if (!origen.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado: A conta de origem não pertence ao seu usuário.");
        }

        if (origen.getId().equals(destino.getId())) {
            throw new IllegalArgumentException("A conta de origem não pode ser igual à conta de destino.");
        }

        transferencia.setUsuario(usuarioLogado);

        origen.setSaldoAtual(origen.getSaldoAtual().subtract(valor));
        destino.setSaldoAtual(destino.getSaldoAtual().add(valor));

        contaRepository.save(origen);
        contaRepository.save(destino);

        transferencia.setContaOrigem(origen);
        transferencia.setContaDestino(destino);
        Transferencia transferenciaSalva = transferenciaRepository.save(transferencia);

        gerarMovimentacaoExtrato(origen, valor, "SAIDA", "Transferência enviada para " + destino.getNome(), transferenciaSalva);
        gerarMovimentacaoExtrato(destino, valor, "ENTRADA", "Transferência recebida de " + origen.getNome(), transferenciaSalva);

        return transferenciaSalva;
    }

    /**
     * MÉTODO AUXILIAR: Centraliza a criação das linhas de movimentação no extrato
     */
    private void gerarMovimentacaoExtrato(Conta conta, BigDecimal valor, String tipo, String historico, Transferencia transf) {
        Movimentacao movimento = new Movimentacao();
        movimento.setDataHora(LocalDateTime.now());
        movimento.setValor(valor);
        movimento.setConta(conta);
        movimento.setTipoMovimentacao(tipo);
        movimento.setHistorico(historico);
        movimento.setTransferencia(transf);

        movimento.setCategoria(null);
        movimento.setCentroCusto(null);

        movimentacaoRepository.save(movimento);
    }
}
