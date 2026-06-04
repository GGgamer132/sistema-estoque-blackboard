package com.estoque.service;

import com.estoque.model.Produto;
import com.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Obter todos os produtos
    public List<Produto> obterTodos() {
        return produtoRepository.findAll();
    }

    // Obter um produto por ID
    public Optional<Produto> obterPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // Salvar um novo produto
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    // Atualizar um produto
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        return produtoRepository.findById(id).map(produto -> {
            produto.setNome(produtoAtualizado.getNome());
            produto.setCategoria(produtoAtualizado.getCategoria());
            produto.setPreco_venda(produtoAtualizado.getPreco_venda());
            produto.setLimiar_critico(produtoAtualizado.getLimiar_critico());
            return produtoRepository.save(produto);
        }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    // Deletar um produto
    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }

}