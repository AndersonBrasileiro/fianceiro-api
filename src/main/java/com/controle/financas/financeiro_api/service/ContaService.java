package com.controle.financas.financeiro_api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.repository.ContaRepository;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    /**
     * REGRA MULTI-TENANT: Retorna apenas as contas do usuário logado.
     * Substitui com segurança a listagem genérica que trazia dados de terceiros.
     */
    public List<Conta> listarTodas() {
        Usuario usuarioLogado = getUsuarioAutenticado();
        return contaRepository.findByUsuarioId(usuarioLogado.getId());
    }

    public List<Conta> listarPorUsuario(Long usuarioId) {
        Usuario usuarioLogado = getUsuarioAutenticado();
        // Validação comercial de segurança: impede bisbilhotar IDs de outros inquilinos
        if (!usuarioLogado.getId().equals(usuarioId)) {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado aos dados deste usuário.");
        }
        return contaRepository.findByUsuarioId(usuarioId);
    }

    /**
     * REGRA MULTI-TENANT: Salva uma conta vinculando-a automaticamente ao dono do token.
     */
    @Transactional
    public Conta salvar(Conta conta) {
        Usuario usuarioLogado = getUsuarioAutenticado();
        conta.setUsuario(usuarioLogado);

        if (conta.getId() == null) {
            conta.setSaldoAtual(conta.getSaldoInicial());
        }
        return contaRepository.save(conta);
    }

    /**
     * MÉTODO AUXILIAR: Centraliza a extração do usuário de dentro do Token JWT
     */
    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }
}
