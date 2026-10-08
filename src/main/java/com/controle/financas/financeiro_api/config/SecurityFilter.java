package com.controle.financas.financeiro_api.config;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.controle.financas.financeiro_api.repository.UsuarioRepository;
import com.controle.financas.financeiro_api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Transforma a classe em um componente gerenciado pelo Spring
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    // Injeção via construtor padrão de fábrica de software
    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Recupera o token enviado no cabeçalho HTTP da requisição
        String token = recuperarToken(request);

        // 2. Se houver um token, faz a validação
        if (token != null && !token.isEmpty()) {
            String email = tokenService.validarToken(token);

            // 3. Se o token for válido e contiver um e-mail, busca o usuário e autentica no Spring
            if (!email.isEmpty()) {
                // Buscaremos o usuário pelo e-mail (usando um casting simples ou buscando como UserDetails futuramente)
                var usuarioOpt = usuarioRepository.findByEmail(email);
                
                if (usuarioOpt.isPresent()) {
                    var usuario = usuarioOpt.get();
                    
                    // Criamos o objeto de autenticação do Spring Security vazia por enquanto (sem perfis/roles ainda)
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, java.util.Collections.emptyList());
                    
                    // Injeta oficialmente o usuário autenticado no contexto de segurança da requisição atual
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 4. Libera a requisição para continuar o seu caminho em direção ao Controller
        filterChain.doFilter(request, response);
    }

    /**
     * MÉTODO AUXILIAR: Extrai o token limpo do formato padrão "Bearer eyJhbGciOi..."
     */
    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
