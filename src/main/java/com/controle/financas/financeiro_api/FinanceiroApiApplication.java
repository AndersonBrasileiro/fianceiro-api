package com.controle.financas.financeiro_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Ativa o cronômetro automático de auditoria de dados
public class FinanceiroApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceiroApiApplication.class, args);
	}

}
