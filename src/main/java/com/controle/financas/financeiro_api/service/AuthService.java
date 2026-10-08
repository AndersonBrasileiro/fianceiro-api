package com.controle.financas.financeiro_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.dto.AuthDTO;
import com.controle.financas.financeiro_api.dto.RegistroDTO;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * REGRA DE NEGÓCIO: Registra um novo usuário criptografando a senha com Bcrypt
     */
    @Transactional
    public Usuario registrar(RegistroDTO dados) {
        // Validação básica: impede e-mails duplicados
        if (usuarioRepository.findByEmail(dados.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado no sistema.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.getNome());
        novoUsuario.setEmail(dados.getEmail());
        
        // CRIPTOGRAFIA: Transforma a senha em um hash irreversível antes de salvar no banco
        String senhaCriptografada = passwordEncoder.encode(dados.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(novoUsuario);
    }

    /**
     * REGRA DE NEGÓCIO: Valida as credenciais e gera o Token JWT caso o login esteja correto
     */
    public String autenticar(AuthDTO dados) {
        Usuario usuario = usuarioRepository.findByEmail(dados.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));

        // Compara a senha digitada em texto puro com a senha criptografada salva no banco
        if (!passwordEncoder.matches(dados.getSenha(), usuario.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        // Se passar pela validação, gera e retorna o Token JWT legítimo
        return tokenService.gerarToken(usuario);
    }
}
