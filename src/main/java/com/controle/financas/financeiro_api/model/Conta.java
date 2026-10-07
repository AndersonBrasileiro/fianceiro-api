package com.controle.financas.financeiro_api.model;

import java.math.BigDecimal;
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
@Table(name = "tb_conta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) // Ex: "NuConta", "Carteira", "Banco do Brasil"
    private String nome;

    @Column(nullable = false, length = 50) // Ex: "Corrente", "Poupança", "Dinheiro em Espécie"
    private String tipoConta;

    @Column(nullable = false, precision = 19, scale = 2) // Usamos BigDecimal para valores monetários (precisão exata)
    private BigDecimal saldoInicial;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoAtual;

    // RELACIONAMENTO: Muitas contas pertencem a um único Usuário
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // RELACIONAMENTO: Muitas contas utilizam uma única Moeda
    @ManyToOne
    @JoinColumn(name = "moeda_id", nullable = false)
    private Moeda javaMoeda;
}