package com.controle.financas.financeiro_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.model.Lancamento;
import com.controle.financas.financeiro_api.model.Movimentacao;
import com.controle.financas.financeiro_api.repository.ContaRepository;
import com.controle.financas.financeiro_api.repository.LancamentoRepository;
import com.controle.financas.financeiro_api.repository.MovimentacaoRepository;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    // Construtor para injeção de dependências profissional (elimina os alertas amarelos)
    public LancamentoService(LancamentoRepository lancamentoRepository, 
                             ContaRepository contaRepository, 
                             MovimentacaoRepository movimentacaoRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.contaRepository = contaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /**
     * REGRA DE NEGÓCIO CORE: Salva um lançamento e dispara a baixa automática caso ele já nasça pago.
     */
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

        if (lancamentoSalvo.isPago()) {
            efetuarBaixa(lancamentoSalvo);
        }

        return lancamentoSalvo;
    }

     /**
     * MÉTODO AUXILIAR: Executa os impactos financeiros da baixa/pagamento
     */
    private void efetuarBaixa(Lancamento lancamento) {
        // CORREÇÃO: Busca a conta completa do banco de dados para carregar o saldo atual real
        Conta conta = contaRepository.findById(lancamento.getConta().getId())
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com o ID informado."));
            
        BigDecimal valor = lancamento.getValor();

        if ("RECEITA".equalsIgnoreCase(lancamento.getTipoLancamento())) {
            conta.setSaldoAtual(conta.getSaldoAtual().add(valor));
        } else if ("DESPESA".equalsIgnoreCase(lancamento.getTipoLancamento())) {
            conta.setSaldoAtual(conta.getSaldoAtual().subtract(valor));
        }

        // Salva a conta com o saldo atualizado
        contaRepository.save(conta);

        // Atualiza a referência da conta dentro do lançamento para salvar os dados consistentes
        lancamento.setConta(conta);

        Movimentacao movimento = new Movimentacao();
        movimento.setDataHora(LocalDateTime.now());
        movimento.setValor(valor);
        movimento.setConta(conta);
        movimento.setLancamento(lancamento);
        movimento.setCategoria(lancamento.getCategoria());
        movimento.setCentroCusto(lancamento.getCentroCusto());
        
        if ("RECEITA".equalsIgnoreCase(lancamento.getTipoLancamento())) {
            movimento.setTipoMovimentacao("ENTRADA");
            movimento.setHistorico("Baixa automática: Recebimento de " + lancamento.getDescricao());
        } else {
            movimento.setTipoMovimentacao("SAIDA");
            movimento.setHistorico("Baixa automática: Pagamento de " + lancamento.getDescricao());
        }

        movimentacaoRepository.save(movimento);
    }
    /**
     * REGRA DE NEGÓCIO: Liquida/Baixa um lançamento que estava em aberto.
     */
    @Transactional
    public Lancamento baixarLancamento(Long id) {
        // 1. Busca o lançamento pelo ID
        Lancamento lancamento = lancamentoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lançamento não encontrado com o ID: " + id));

        // 2. Validação: Impede baixar uma conta que já foi paga anteriormente
        if (lancamento.isPago()) {
            throw new IllegalStateException("Este lançamento já encontra-se baixado/pago.");
        }

        // 3. Atualiza o status do título para o momento atual
        lancamento.setPago(true);
        lancamento.setDataPagamento(java.time.LocalDate.now());

        // 4. Salva a alteração do título
        Lancamento lancamentoAtualizado = lancamentoRepository.save(lancamento);

        // 5. Dispara o impacto financeiro (reutilizando a lógica que criamos de saldo e extrato)
        efetuarBaixa(lancamentoAtualizado);

        return lancamentoAtualizado;
    }
}