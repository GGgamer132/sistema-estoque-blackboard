package com.estoque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sugestao_reposicao")
public class SugestaoReposicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TRANSFERENCIA ou ORDEM_COMPRA
    @Column(nullable = false, length = 50)
    private String tipoAcao;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Loja que está com estoque crítico (destino)
    @ManyToOne
    @JoinColumn(name = "loja_destino_id", nullable = false)
    private Loja lojaDestino;

    // Loja que vai ceder estoque (só para transferências)
    @ManyToOne
    @JoinColumn(name = "loja_origem_id")
    private Loja lojaOrigem;

    // Fornecedor sugerido (só para ordens de compra)
    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Column(nullable = false)
    private Integer quantidadeRecomendada;

    // Explica por que essa sugestão foi gerada
    @Column(columnDefinition = "TEXT")
    private String justificativa;

    // PENDENTE, APROVADA, REJEITADA, EXECUTADA
    @Column(length = 50)
    private String status = "PENDENTE";

    @Column
    private LocalDateTime dataCriacao;

    @PrePersist
    public void preencheDataCriacao() {
        this.dataCriacao = LocalDateTime.now();
    }
}