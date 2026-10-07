package com.controle.financas.financeiro_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.model.Movimentacao;
import com.controle.financas.financeiro_api.model.Transferencia;
import com.controle.financas.financeiro_api.repository.ContaRepository;
import com.controle.financas.financeiro_api.repository.MovimentacaoRepository;
import com.controle.financas.financeiro_api.repository.TransferenciaRepository;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    // Injeção via construtor padrão de mercado
    public TransferenciaService(TransferenciaRepository transferenciaRepository, 
                                ContaRepository contaRepository, 
                                MovimentacaoRepository movimentacaoRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /**
     * REGRA DE NEGÓCIO CORE: Efetua uma transferência entre contas blindando os saldos
     */
    @Transactional
    public Transferencia transferir(Transferencia transferencia) {
        BigDecimal valor = transferencia.getValor();

        // 1. Validação básica: Impede transferências com valor zero ou negativo
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }

        // 2. Busca as contas origem e destino atualizadas direto do banco de dados
        Conta origem = contaRepository.findById(transferencia.getContaOrigem().getId())
            .orElseThrow(() -> new IllegalArgumentException("Conta origem não encontrada."));
            
        Conta destino = contaRepository.findById(transferencia.getContaDestino().getId())
            .orElseThrow(() -> new IllegalArgumentException("Conta destino não encontrada."));

        // 3. Validação Comercial: Impede transferir para a mesma conta
        if (origem.getId().equals(destino.getId())) {
            throw new IllegalArgumentException("A conta de origem não pode ser igual à conta de destino.");
        }

        // 4. Atualiza os saldos bancários das entidades
        origem.setSaldoAtual(origem.getSaldoAtual().subtract(valor));
        destino.setSaldoAtual(destino.getSaldoAtual().add(valor));

        // 5. Salva as duas contas com os novos saldos na memória do banco
        contaRepository.save(origem);
        contaRepository.save(destino);

        // 6. Atualiza as referências completas dentro do objeto transferência e salva
        transferencia.setContaOrigem(origem);
        transferencia.setContaDestino(destino);
        Transferencia transferenciaSalva = transferenciaRepository.save(transferencia);

        // 7. GATILHO DUPLO: Gera os dois registros independentes no Extrato Interno (Muitos-para-Um)
        gerarMovimentacaoExtrato(origem, valor, "SAIDA", "Transferência enviada para " + destino.getNome(), transferenciaSalva);
        gerarMovimentacaoExtrato(destino, valor, "ENTRADA", "Transferência recebida de " + origem.getNome(), transferenciaSalva);

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
        movimento.setTransferencia(transf); // Amarração da chave estrangeira

        // Como transferências são movimentações de carteira (não envolvem compras comerciais),
        // passamos referências nulas para Categoria e Centro de Custo neste fluxo interno.
        movimento.setCategoria(null);
        movimento.setCentroCusto(null);

        movimentacaoRepository.save(movimento);
    }
}