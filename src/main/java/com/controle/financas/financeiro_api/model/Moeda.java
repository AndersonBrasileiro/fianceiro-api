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
@Table(name = "tb_moeda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Moeda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50) // Ex: Real, Dólar, Euro
    private String nome;

    @Column(nullable = false, length = 3) // Ex: BRL, USD, EUR
    private String codigoIso;

    @Column(nullable = false, length = 5) // Ex: R$, $, €
    private String simbolo;

    // 🌟 NOVO CAMPO DE ISOLAMENTO: Vincula a moeda de forma física ao usuário dono da licença
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
