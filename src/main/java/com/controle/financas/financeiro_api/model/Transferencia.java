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
@Table(name = "tb_transferencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150) // Ex: "Ajuste Poupança para Corrente", "Pix para Investimentos"
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate dataTransferencia;

    // --- RELACIONAMENTOS ---

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id", nullable = false) // De onde sai o dinheiro
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id", nullable = false) // Para onde vai o dinheiro
    private Conta contaDestino;
}