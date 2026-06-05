package com.estoque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "estoque",
    uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id", "loja_id"})
)
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;


    @ManyToOne
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;


    @Column(nullable = false)
    private Integer quantidadeAtual;

}