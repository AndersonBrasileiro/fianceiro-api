package com.controle.financas.financeiro_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.repository.ContaRepository;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }

    public List<Conta> listarPorUsuario(Long usuarioId) {
        return contaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public Conta salvar(Conta conta) {
        // Regra comercial de inicialização: o saldo atual nasce igual ao saldo inicial
        if (conta.getId() == null) {
            conta.setSaldoAtual(conta.getSaldoInicial());
        }
        return contaRepository.save(conta);
    }
}
