package com.controle.financas.financeiro_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    // Injeção via construtor padrão da fábrica
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF (padrão em APIs REST com JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API sem guardar estado no servidor
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Mantém liberação para exibição do painel H2
            .authorizeHttpRequests(auth -> auth
                // REGRA 1: Libera o endpoint público do H2-Console para podermos analisar o banco em desenvolvimento
                .requestMatchers("/h2-console/**").permitAll()
                
                // REGRA 2: Libera as futuras rotas públicas de Autenticação (Login e Cadastro de Usuário)
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/registrar").permitAll()
                
                // REGRA 3: Bloqueia qualquer outro endpoint comercial do sistema, exigindo token JWT válido
                .anyRequest().authenticated()
            )
            // REGRA 4: Adiciona o nosso filtro customizado de validação JWT exatamente antes do filtro padrão do Spring
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * BEAN ADICIONAL: Configura o gerenciador de autenticação do Spring Security
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * BEAN ADICIONAL: Algoritmo de criptografia Bcrypt para salvar senhas de forma segura e irreversível no banco
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
