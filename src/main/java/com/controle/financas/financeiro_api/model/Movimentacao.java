package com.controle.financas.financeiro_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_movimentacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora; // Momento exato em que o dinheiro se moveu

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor; // Valor real movimentado

    @Column(nullable = false, length = 20) // "ENTRADA" ou "SAIDA"
    private String tipoMovimentacao;

    @Column(length = 255) // Descrição amigável do extrato
    private String historico;

    // =======================================================================
    //                     MAPEAMENTO DE RELACIONAMENTOS (JPA)
    // =======================================================================

    /**
     * RELACIONAMENTO: Muitas movimentações pertencem a uma única CONTA.
     * Define qual saldo bancário ou carteira foi alterada.
     */
    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    /**
     * RELACIONAMENTO: Muitas movimentações podem estar atreladas a um LANÇAMENTO.
     * Opcional (nullable = true), pois há movimentos sem conta a pagar associada (ex: transferências).
     */
    @ManyToOne
    @JoinColumn(name = "lancamento_id", nullable = true)
    private Lancamento lancamento;

    /**
     * RELACIONAMENTO: Muitas movimentações podem ter nascido de uma TRANSFERÊNCIA entre contas.
     * Opcional (nullable = true), pois o movimento pode vir de um Lancamento tradicional.
     */
    @ManyToOne
    @JoinColumn(name = "transferencia_id", nullable = true)
    private Transferencia transferencia;

    /**
     * RELACIONAMENTO: Muitas movimentações possuem uma CATEGORIA (Subcategoria).
     * Essencial para puxar gráficos de extrato por categoria de forma ultra rápida.
     * Obrigatório (nullable = false) para garantir consistência nos relatórios.
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = true) // Alterado para true para aceitar transferências!
    private Categoria categoria;

    /**
     * RELACIONAMENTO: Muitas movimentações podem ter um CENTRO DE CUSTO/RECEITA.
     * Opcional (nullable = true), permitindo filtrar o extrato por projetos ou fins pessoais/profissionais.
     */
    @ManyToOne
    @JoinColumn(name = "centro_custo_id", nullable = true)
    private CentroCusto centroCusto;
}