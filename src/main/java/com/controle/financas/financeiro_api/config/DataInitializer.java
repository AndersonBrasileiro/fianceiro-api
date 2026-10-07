package com.controle.financas.financeiro_api.config;

import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.controle.financas.financeiro_api.model.Categoria;
import com.controle.financas.financeiro_api.model.Conta;
import com.controle.financas.financeiro_api.model.Moeda;
import com.controle.financas.financeiro_api.model.Usuario;
import com.controle.financas.financeiro_api.repository.CategoriaRepository;
import com.controle.financas.financeiro_api.repository.ContaRepository;
import com.controle.financas.financeiro_api.repository.MoedaRepository;
import com.controle.financas.financeiro_api.repository.UsuarioRepository;

@Component // Diz ao Spring para gerenciar e executar esta classe automaticamente
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final MoedaRepository moedaRepository;
    private final ContaRepository contaRepository;
    private final CategoriaRepository categoriaRepository;

    public DataInitializer(UsuarioRepository usuarioRepository, 
                           MoedaRepository moedaRepository,
                           ContaRepository contaRepository, 
                           CategoriaRepository categoriaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.moedaRepository = moedaRepository;
        this.contaRepository = contaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("====== INICIANDO CARGA PRÉVIA DE DADOS PARA TESTES ======");

        // 1. Injeta Usuário Padrão (ID 1)
        Usuario usuario = new Usuario();
        usuario.setNome("Desenvolvedor Comercial");
        usuario.setEmail("dev@sistema.com");
        usuario.setSenha("123456"); // No futuro será criptografada
        usuario = usuarioRepository.save(usuario);

        // 2. Injeta Moeda Real (ID 1)
        Moeda real = new Moeda();
        real.setNome("Real");
        real.setCodigoIso("BRL");
        real.setSimbolo("R$");
        real = moedaRepository.save(real);

        // 3. Injeta Conta Corrente vinculada (ID 1)
        Conta conta = new Conta();
        conta.setNome("Nubank Principal");
        conta.setTipoConta("Corrente");
        conta.setSaldoInicial(new BigDecimal("1000.00"));
        conta.setSaldoAtual(new BigDecimal("1000.00")); // Começa com 1000 reais
        conta.setUsuario(usuario);
        conta.setJavaMoeda(real);
        conta = contaRepository.save(conta);

        // 4. Injeta Categoria Pai (ID 1) e Subcategoria (ID 2)
        Categoria categoriaPai = new Categoria();
        categoriaPai.setNome("Habitação");
        categoriaPai.setCategoriaPai(null);
        categoriaPai = categoriaRepository.save(categoriaPai);

        Categoria subcategoria = new Categoria();
        subcategoria.setNome("Aluguel");
        subcategoria.setCategoriaPai(categoriaPai); // Vincula a subcategoria ao pai
        subcategoria = categoriaRepository.save(subcategoria);

        System.out.println("====== CARGA DE DADOS CONCLUÍDA COM SUCESSO ======");
    }
}