package com.estoque.service;
import com.estoque.model.Estoque;
import com.estoque.model.Loja;
import com.estoque.model.Produto;
import com.estoque.model.Venda;
import com.estoque.repository.EstoqueRepository;
import com.estoque.repository.LojaRepository;
import com.estoque.repository.ProdutoRepository;
import com.estoque.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialistaLojaService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private LojaRepository lojaRepository;

    @Transactional
    public Venda registrarVenda(Long produtoId, Long lojaId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException(
                "Produto não encontrado: ID " + produtoId));
        
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new RuntimeException(
                "Loja não encontrada: ID " + lojaId));

        Estoque estoque = estoqueRepository.findByProdutoAndLoja(produto, loja)
            .orElseThrow(() -> new RuntimeException(
                "Estoque não encontrado para produto " + produto.getNome()
                + " na loja " + loja.getNome()));
        
        if (estoque.getQuantidadeAtual() < quantidade) {
            throw new RuntimeException(
                "Estoque insuficiente! Disponível: "
                + estoque.getQuantidadeAtual()
                + " | Solicitado: " + quantidade);
        }

        // ─── PASSO 1: Registra a venda no histórico (Blackboard) 
        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setLoja(loja);
        venda.setQuantidade(quantidade);
        venda.setPrecoUnitario(produto.getPreco_venda());
        vendaRepository.save(venda);
        // ─── PASSO 2: Atualiza o estoque no Blackboard ───
        // É aqui que o trigger vai "ver" a mudança e agir se necessário
        int novaQuantidade = estoque.getQuantidadeAtual() - quantidade;
        estoque.setQuantidadeAtual(novaQuantidade);
        estoqueRepository.save(estoque);

        System.out.println("[LOJA " + loja.getNome() + "] Venda registrada: "
            + quantidade + "x " + produto.getNome()
            + " | Novo estoque: " + novaQuantidade
            + " | Limiar crítico: " + produto.getLimiar_critico());

        return venda;
    }
}