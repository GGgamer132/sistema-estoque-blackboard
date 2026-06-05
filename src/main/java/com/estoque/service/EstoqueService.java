package com.estoque.service;
import com.estoque.model.Estoque;
import com.estoque.model.Loja;
import com.estoque.model.Produto;
import com.estoque.repository.EstoqueRepository;
import com.estoque.repository.LojaRepository;
import com.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private LojaRepository lojaRepository;

    public List<Estoque> obterTodos() {
        return estoqueRepository.findAll();
    }

    public List<Estoque> obterPorLoja(Long lojaId) {
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new RuntimeException("Loja não encontrada: ID " + lojaId));
        return estoqueRepository.findByLoja(loja);
    }

    public List<Estoque> obterPorProduto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado: ID " + produtoId));
        return estoqueRepository.findByProduto(produto);
    }

    /**
     * Cadastra o estoque inicial de um produto em uma loja.
     * Esse método é usado para configurar o Blackboard com os dados iniciais.
     * Nas operações do dia a dia, o estoque é atualizado pelo EspecialistaLojaService.
     */
    public Estoque cadastrarEstoqueInicial(Long produtoId, Long lojaId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado: ID " + produtoId));
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new RuntimeException("Loja não encontrada: ID " + lojaId));

        // Verifica se já existe um registro de estoque para esse produto nessa loja
        return estoqueRepository.findByProdutoAndLoja(produto, loja).map(estoqueExistente -> {
            // Se já existe, apenas atualiza a quantidade
            estoqueExistente.setQuantidadeAtual(quantidade);
            return estoqueRepository.save(estoqueExistente);
        }).orElseGet(() -> {
            // Se não existe, cria um novo registro no Blackboard
            Estoque novoEstoque = new Estoque();
            novoEstoque.setProduto(produto);
            novoEstoque.setLoja(loja);
            novoEstoque.setQuantidadeAtual(quantidade);
            return estoqueRepository.save(novoEstoque);
        });
    }
}