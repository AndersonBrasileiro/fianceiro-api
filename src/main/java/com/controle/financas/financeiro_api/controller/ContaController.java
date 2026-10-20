package com.controle.financas.financeiro_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.model.Moeda;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.repository.MoedaRepository;
import com.controle.financas.financeiro_api.service.ContaService;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class ContaController {

    private final ContaService contaService;
    // 🌟 INJEÇÃO ADICIONAL: Permite buscar a moeda autoincrementável real do usuário logado
    private final MoedaRepository moedaRepository;

    public ContaController(ContaService contaService, MoedaRepository moedaRepository) {
        this.contaService = contaService;
        this.moedaRepository = moedaRepository;
    }

    // 🌟 ATUALIZADO COM TOKEN: Agora lista apenas as contas pertencentes ao usuário dono do Token JWT
    @GetMapping
    public ResponseEntity<List<Conta>> buscarTodas(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(contaService.listarPorUsuario(usuarioLogado.getId()));
    }

    // Mantido por compatibilidade interna se necessário, mas o front usará a rota acima!
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Conta>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(contaService.listarPorUsuario(usuarioId));
    }

    // 🌟 ATUALIZADO COM BLINDAGEM MULTI-TENANT: Ignora o ID fixo do front e injeta a moeda correta do usuário!
    @PostMapping
    public ResponseEntity<Conta> criar(@RequestBody Conta conta, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        
        // 1. Vincula fisicamente a nova conta bancária ao usuário logado
        conta.setUsuario(usuarioLogado);

        // 2. DESTRAVAMENTO DA MOEDA: Busca no banco a moeda 'BRL' que o próprio Java criou para este usuário
        Moeda moedaDoUsuario = moedaRepository.findByCodigoIsoAndUsuarioId("BRL", usuarioLogado.getId())
            .orElseThrow(() -> new IllegalArgumentException("Moeda padrão BRL não encontrada para este usuário."));

        // 3. Força a amarração com a moeda correta (ID 1, ID 2, etc.) ignorando qualquer trapaça ou valor fixo do front
        conta.setJavaMoeda(moedaDoUsuario);

        Conta novaConta = contaService.salvar(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }
}
