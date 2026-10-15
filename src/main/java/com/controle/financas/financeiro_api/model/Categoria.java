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
@Table(name = "tb_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    // 🌟 NOVO CAMPO: Identifica se a categoria pertence ao fluxo de RECEITA ou DESPESA
    @Column(nullable = false, length = 20)
    private String tipo;

    // AUTO-RELACIONAMENTO: Muitas subcategorias pertencem a uma Categoria Pai
    @ManyToOne
    @JoinColumn(name = "categoria_pai_id", nullable = true)
    private Categoria categoriaPai;
}
