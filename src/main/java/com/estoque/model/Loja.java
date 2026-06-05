package com.estoque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loja")
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da loja (ex: "Loja Norte", "Loja Sul")
    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 255)
    private String endereco;
}