package com.controle.financas.financeiro_api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErroRespostaDTO {
    private LocalDateTime timestamp;
    private int status; // Ex: 400, 403, 404
    private String erro;  // Título do erro (Ex: "Acesso Negado")
    private String mensagem; // Explicação legível do erro (Ex: "A conta de origem não pertence...")
    private String caminho;  // Rota HTTP onde o erro aconteceu (Ex: "/api/transferencias")
}
