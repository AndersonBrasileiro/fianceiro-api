package com.controle.financas.financeiro_api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.controle.financas.financeiro_api.model.Usuario;

@Service
public class TokenService {

    // Lê uma assinatura secreta direto do application.properties para aumentar a segurança comercial.
    // Se não houver nada configurado, usa um valor padrão por segurança.
    @Value("${api.security.token.secret:assinatura-secreta-do-sistema-financeiro}")
    private String secret;

    /**
     * REGRA DE NEGÓCIO: Gera um Token JWT único e criptografado para o usuário logado
     */
    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("financeiro-api") // Identifica quem emitiu o token
                    .withSubject(usuario.getEmail()) // Guarda o email do dono do token
                    .withExpiresAt(gerarDataExpiracao()) // Define o tempo de validade do token (ex: 2 horas)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token de autenticação", exception);
        }
    }

    /**
     * REGRA DE NEGÓCIO: Valida se o token recebido da internet é verdadeiro e extrai o email contido nele
     */
    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("financeiro-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o email do usuário caso o token seja válido
        } catch (JWTVerificationException exception) {
            return ""; // Retorna string vazia indicando token inválido ou expirado
        }
    }

    /**
     * MÉTODO AUXILIAR: Define o tempo de vida do token. No mercado comercial, costuma-se expirar em 2 horas.
     */
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
