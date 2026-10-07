package com.controle.financas.financeiro_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_usuario")
@Data // Cria automaticamente Getters, Setters, equals, hashCode e toString via Lombok
@NoArgsConstructor // Cria o construtor vazio exigido pelo JPA
@AllArgsConstructor // Cria o construtor com todos os campos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco vai gerar o ID automaticamente (1, 2, 3...)
    private Long id;

    @Column(nullable = false, length = 100) // Campo obrigatório no banco
    private String nome;

    @Column(nullable = false, unique = true, length = 100) // Não permite emails duplicados no sistema
    private String email;

    @Column(nullable = false, length = 255) // Tamanho maior pensando na senha criptografada no futuro
    private String senha;
}