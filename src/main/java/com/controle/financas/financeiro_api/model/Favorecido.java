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
@Table(name = "tb_favorecido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorecido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150) // Nome da pessoa ou empresa
    private String nome;

    @Column(length = 20) // Campo opcional para CPF ou CNPJ, caso queira tornar o sistema mais robusto
    private String cpfCnpj;

    // RELACIONAMENTO: Cada usuário tem a sua própria lista de favorecidos/contatos
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}