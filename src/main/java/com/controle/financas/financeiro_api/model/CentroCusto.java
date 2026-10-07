package com.controle.financas.financeiro_api.model;

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
@Table(name = "tb_centro_custo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentroCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) // Ex: "Projeto A", "Filial Sul", "Uso Pessoal"
    private String nome;

    @Column(nullable = false, length = 20) // REGRA: Guardará "DESPESA", "RECEITA" ou "AMBOS"
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}