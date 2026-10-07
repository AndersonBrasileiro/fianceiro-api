package com.controle.financas.financeiro_api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF para testes locais
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Permite exibir o console H2
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Libera temporariamente todas as rotas e o H2
            );
        return http.build();
    }
}