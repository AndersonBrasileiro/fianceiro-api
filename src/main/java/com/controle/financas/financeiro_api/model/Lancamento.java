package com.controle.financas.financeiro_api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "tb_lancamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150) // Ex: "Condomínio Ed. Real", "Venda de Consultoria"
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2) // Precisão decimal exata para valores monetários
    private BigDecimal valor;

    @Column(nullable = false, length = 20) // Guardará a etiqueta: "DESPESA" ou "RECEITA"
    private String tipoLancamento;

    @Column(nullable = false)
    private LocalDate dataVencimento; // Data máxima para pagar ou receber

    private LocalDate dataPagamento; // Fica null (vazio) enquanto a conta estiver em aberto

    @Column(nullable = false)
    private boolean pago; // false = Em Aberto / true = Baixado (Pago/Recebido)

    // =======================================================================
    //                     MAPEAMENTO DE RELACIONAMENTOS (JPA)
    // =======================================================================

    /**
     * RELACIONAMENTO: Muitas contas a pagar/receber pertencem a um único USUÁRIO.
     * Garante o isolamento (Multi-tenant): o usuário só vê os lançamentos dele.
     * No banco, gera a coluna chave estrangeira: 'usuario_id' (Não Aceita Nulo).
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * RELACIONAMENTO: Muitos lançamentos movimentam uma única CONTA (carteira, banco, etc.).
     * Define de onde sairá o dinheiro (se despesa) ou para onde irá (se receita).
     * No banco, gera a coluna chave estrangeira: 'conta_id' (Não Aceita Nulo).
     */
    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    /**
     * RELACIONAMENTO: Muitos lançamentos são classificados em uma única CATEGORIA.
     * Como regra de negócio, salvaremos aqui o ID da SUBCATEGORIA escolhida pelo usuário.
     * No banco, gera a coluna chave estrangeira: 'categoria_id' (Não Aceita Nulo).
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /**
     * RELACIONAMENTO: Muitos lançamentos podem ter um único FAVORECIDO.
     * Indica para quem pagamos ou de quem recebemos.
     * No banco, gera a coluna: 'favorecido_id' (nullable = true, pois o campo é OPCIONAL).
     */
    @ManyToOne
    @JoinColumn(name = "favorecido_id", nullable = true)
    private Favorecido favorecido;

    /**
     * RELACIONAMENTO: Muitos lançamentos podem pertencer a um único CENTRO DE CUSTO/RECEITA.
     * Permite agrupar transações por projetos, filiais, fins pessoais ou profissionais.
     * No banco, gera a coluna: 'centro_custo_id' (nullable = true, pois também é OPCIONAL).
     */
    @ManyToOne
    @JoinColumn(name = "centro_custo_id", nullable = true)
    private CentroCusto centroCusto;
}