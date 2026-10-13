package com.controle.financas.financeiro_api.config;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.controle.financas.financeiro_api.dto.ErroRespostaDTO;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice // Avisa ao Spring Boot para centralizar o tratamento de erros aqui
public class GlobalExceptionHandler {

    /**
     * CAPTURA: Erros de segurança (AccessDeniedException) - Erro 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroRespostaDTO> tratarAcessoNegado(AccessDeniedException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Acesso Proibido",
            ex.getMessage(), // Captura a mensagem exata que escrevemos no Service!
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    /**
     * CAPTURA: Dados inválidos passados pelo usuário (IllegalArgumentException) - Erro 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroRespostaDTO> tratarArgumentoInvalido(IllegalArgumentException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Requisição Inválida",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    /**
     * CAPTURA: Operações ilegais no fluxo de negócios (IllegalStateException) - Erro 400
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroRespostaDTO> tratarEstadoIlegal(IllegalStateException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Regra de Negócio Violada",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
