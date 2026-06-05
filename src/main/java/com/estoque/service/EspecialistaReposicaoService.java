package com.estoque.service;

import com.estoque.model.*;
import com.estoque.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EspecialistaReposicaoService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private FornecedorCatalogoRepository fornecedorCatalogoRepository;

    @Autowired
    private SugestaoReposicaoRepository sugestaoReposicaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private LojaRepository lojaRepository;

    /**
     * Método principal acionado pelo trigger via pg_notify.
     * Recebe o ID do produto e da loja que ficaram com estoque crítico.
     */
    public void analisarReposicao(Long produtoId, Long lojaDestinoId) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow();
        Loja lojaDestino = lojaRepository.findById(lojaDestinoId).orElseThrow();
        Estoque estoqueCritico = estoqueRepository
            .findByProdutoAndLoja(produto, lojaDestino).orElseThrow();

        // Calcula a quantidade recomendada de reposição
        // (dobro do limiar crítico é uma heurística simples e boa para demonstração)
        int quantidadeRecomendada = produto.getLimiar_critico() * 2;

        // Primeira tentativa: verificar se outra loja tem excedente
        List<Estoque> estoquesDaRede = estoqueRepository.findByProduto(produto);

        for (Estoque estoque : estoquesDaRede) {
            // Ignora a loja que está com problema
            if (estoque.getLoja().getId().equals(lojaDestinoId)) continue;
            // Verifica se essa loja tem excedente (mais que o dobro do limiar)
            boolean temExcedente = estoque.getQuantidadeAtual()
                > produto.getLimiar_critico() * 2;
            if (temExcedente) {
                // Gera sugestão de TRANSFERÊNCIA entre lojas
                SugestaoReposicao sugestao = new SugestaoReposicao();
                sugestao.setTipoAcao("TRANSFERENCIA");
                sugestao.setProduto(produto);
                sugestao.setLojaOrigem(estoque.getLoja());
                sugestao.setLojaDestino(lojaDestino);
                sugestao.setQuantidadeRecomendada(quantidadeRecomendada);
                sugestao.setJustificativa(
                    "Estoque crítico em " + lojaDestino.getNome() +
                    " (" + estoqueCritico.getQuantidadeAtual() + " unidades). " +
                    estoque.getLoja().getNome() + " tem excedente de " +
                    estoque.getQuantidadeAtual() + " unidades."
                );
                sugestaoReposicaoRepository.save(sugestao);
                return;
            }
        }

        // Segunda tentativa: sem excedente em nenhuma loja → sugere compra
        List<FornecedorCatalogo> fornecedores =
            fornecedorCatalogoRepository.findByProduto(produto);
        if (!fornecedores.isEmpty()) {
            // Escolhe o fornecedor com menor preço
            FornecedorCatalogo melhorOpcao = fornecedores.stream()
                .min((a, b) -> a.getPrecoCompra().compareTo(b.getPrecoCompra())).orElseThrow();
            SugestaoReposicao sugestao = new SugestaoReposicao();
            sugestao.setTipoAcao("ORDEM_COMPRA");
            sugestao.setProduto(produto);
            sugestao.setLojaDestino(lojaDestino);
            sugestao.setFornecedor(melhorOpcao.getFornecedor());
            sugestao.setQuantidadeRecomendada(quantidadeRecomendada);
            sugestao.setJustificativa(
                "Estoque crítico em " + lojaDestino.getNome() +
                " (" + estoqueCritico.getQuantidadeAtual() + " unidades). " +
                "Sem excedente em outras lojas. Fornecedor sugerido: " +
                melhorOpcao.getFornecedor().getNome() + " com preço de R$ " + 
                melhorOpcao.getPrecoCompra() + " por unidade."
            );
            sugestaoReposicaoRepository.save(sugestao);
        }
    }
}