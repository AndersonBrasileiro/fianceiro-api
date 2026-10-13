package com.controle.financas.financeiro_api.service;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.controle.financas.financeiro_api.model.CentroCusto;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.repository.CentroCustoRepository;

@Service
public class CentroCustoService {

    private final CentroCustoRepository centroCustoRepository;

    public CentroCustoService(CentroCustoRepository centroCustoRepository) {
        this.centroCustoRepository = centroCustoRepository;
    }

    /**
     * REGRA DE NEGÓCIO MULTI-TENANT: Salva um Centro de Custo vinculando-o ao usuário logado.
     */
    @Transactional
    public CentroCusto salvar(CentroCusto centroCusto) {
        Usuario usuarioLogado = getUsuarioAutenticado();
        centroCusto.setUsuario(usuarioLogado);
        
        // Força o tipo a ficar em maiúsculo para evitar erros de digitação (Ex: "despesa" -> "DESPESA")
        centroCusto.setTipo(centroCusto.getTipo().toUpperCase());
        
        return centroCustoRepository.save(centroCusto);
    }

    /**
     * REGRA DE NEGÓCIO CORE SIMPLIFICADA: Filtra os centros com base no fluxo exato do lançamento.
     * Se o fluxo da tela for "DESPESA", busca apenas as caixas marcadas como DESPESA.
     * Se o fluxo da tela for "RECEITA", busca apenas as caixas marcadas como RECEITA.
     */
    public List<CentroCusto> listarPorFluxoELogado(String fluxoLancamento) {
        Usuario usuarioLogado = getUsuarioAutenticado();
        String fluxo = fluxoLancamento.toUpperCase();

        // Validação de segurança de dados: impede o envio de lixo ou tipos inválidos pela URL
        if (!"DESPESA".equals(fluxo) && !"RECEITA".equals(fluxo)) {
            throw new IllegalArgumentException("Fluxo de lançamento inválido. Use estritamente 'DESPESA' ou 'RECEITA'.");
        }

        // Busca de forma direta passando apenas um único tipo na lista filtrada por inquilino
        return centroCustoRepository.findByUsuarioIdAndTipoIn(
            usuarioLogado.getId(), 
            java.util.Collections.singletonList(fluxo)
        );
    }

    /**
     * MÉTODO AUXILIAR: Extrai o usuário logado do Token JWT
     */
    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }
}
