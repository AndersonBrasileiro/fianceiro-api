package com.controle.financas.financeiro_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.controle.financas.financeiro_api.dto.AuthDTO;
import com.controle.financas.financeiro_api.dto.RegistroDTO;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.service.AuthService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ENDPOINT PÚBLICO: Permite novos cadastros na plataforma.
     * Rota: POST http://localhost:8080/api/auth/registrar
     */
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistroDTO dados) {
        Usuario novoUsuario = authService.registrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    /**
     * ENDPOINT PÚBLICO: Realiza o login e devolve o Token JWT.
     * Rota: POST http://localhost:8080/api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthDTO dados) {
        String token = authService.autenticar(dados);
        // Retorna o token envelopado de forma amigável em um JSON
        return ResponseEntity.ok(Map.of("token", token));
    }
}

