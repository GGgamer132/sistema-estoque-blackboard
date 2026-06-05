package com.estoque.service;
import com.estoque.model.Estoque;
import com.estoque.model.SugestaoReposicao;
import com.estoque.repository.EstoqueRepository;
import com.estoque.repository.SugestaoReposicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SugestaoReposicaoService {

    @Autowired
    private SugestaoReposicaoRepository sugestaoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    public List<SugestaoReposicao> obterTodas() {
        return sugestaoRepository.findAll();
    }

    public List<SugestaoReposicao> obterPendentes() {
        return sugestaoRepository.findByStatus("PENDENTE");
    }

    public Optional<SugestaoReposicao> obterPorId(Long id) {
        return sugestaoRepository.findById(id);
    }

    public SugestaoReposicao aprovar(Long id) {
        return atualizarStatus(id, "APROVADA");
    }

    public SugestaoReposicao rejeitar(Long id) {
        return atualizarStatus(id, "REJEITADA");
    }

    /*
     * Marca a sugestão como executada E atualiza o Blackboard com as novas
     * quantidades de estoque, refletindo a ação que o gerente realizou.
     */
    @Transactional
    public SugestaoReposicao marcarExecutada(Long id) {
        SugestaoReposicao sugestao = sugestaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sugestão não encontrada: ID " + id));
        if ("TRANSFERENCIA".equals(sugestao.getTipoAcao())) {
            executarTransferencia(sugestao);
        } else if ("ORDEM_COMPRA".equals(sugestao.getTipoAcao())) {
            executarOrdemCompra(sugestao);
        }
        sugestao.setStatus("EXECUTADA");
        return sugestaoRepository.save(sugestao);
    }

    /**
     * Executa a transferência entre lojas:
     * - Loja de ORIGEM perde as unidades transferidas
     * - Loja de DESTINO recebe as unidades transferidas
     */
    private void executarTransferencia(SugestaoReposicao sugestao) {
        int quantidade = sugestao.getQuantidadeRecomendada();
        // Atualiza o estoque da loja de ORIGEM (perde estoque)
        Estoque estoqueOrigem = estoqueRepository
            .findByProdutoAndLoja(sugestao.getProduto(), sugestao.getLojaOrigem())
            .orElseThrow(() -> new RuntimeException(
                "Estoque de origem não encontrado para o produto "
                + sugestao.getProduto().getNome()
                + " na loja " + sugestao.getLojaOrigem().getNome()));
        
        int novaQuantidadeOrigem = estoqueOrigem.getQuantidadeAtual() - quantidade;
        if (novaQuantidadeOrigem < 0) {
            throw new RuntimeException(
                "Estoque insuficiente na loja de origem para realizar a transferência. "
                + "Disponível: " + estoqueOrigem.getQuantidadeAtual()
                + " | Necessário: " + quantidade);
        }
        estoqueOrigem.setQuantidadeAtual(novaQuantidadeOrigem);
        estoqueRepository.save(estoqueOrigem);
        // Atualiza o estoque da loja de DESTINO (recebe estoque)
        Estoque estoqueDestino = estoqueRepository
            .findByProdutoAndLoja(sugestao.getProduto(), sugestao.getLojaDestino())
            .orElseThrow(() -> new RuntimeException(
                "Estoque de destino não encontrado para o produto "
                + sugestao.getProduto().getNome()
                + " na loja " + sugestao.getLojaDestino().getNome()));

        estoqueDestino.setQuantidadeAtual(estoqueDestino.getQuantidadeAtual() + quantidade);
        estoqueRepository.save(estoqueDestino);
        System.out.println("[BLACKBOARD] Transferência executada: "
            + quantidade + "x " + sugestao.getProduto().getNome()
            + " | De: " + sugestao.getLojaOrigem().getNome()
            + " → Para: " + sugestao.getLojaDestino().getNome()
            + " | Novo estoque origem: " + novaQuantidadeOrigem
            + " | Novo estoque destino: " + estoqueDestino.getQuantidadeAtual());
    }

    /*
     * Executa a ordem de compra:
     * - Loja de DESTINO recebe os produtos comprados do fornecedor
     */
    private void executarOrdemCompra(SugestaoReposicao sugestao) {
        int quantidade = sugestao.getQuantidadeRecomendada();
        Estoque estoqueDestino = estoqueRepository
            .findByProdutoAndLoja(sugestao.getProduto(), sugestao.getLojaDestino())
            .orElseThrow(() -> new RuntimeException(
                "Estoque de destino não encontrado para o produto "
                + sugestao.getProduto().getNome()
                + " na loja " + sugestao.getLojaDestino().getNome()));

        estoqueDestino.setQuantidadeAtual(estoqueDestino.getQuantidadeAtual() + quantidade);
        estoqueRepository.save(estoqueDestino);
        System.out.println("[BLACKBOARD] Ordem de compra executada: "
            + quantidade + "x " + sugestao.getProduto().getNome()
            + " | Fornecedor: " + sugestao.getFornecedor().getNome()
            + " | Loja destino: " + sugestao.getLojaDestino().getNome()
            + " | Novo estoque: " + estoqueDestino.getQuantidadeAtual());
    }

    private SugestaoReposicao atualizarStatus(Long id, String novoStatus) {
        return sugestaoRepository.findById(id).map(sugestao -> {
            sugestao.setStatus(novoStatus);
            return sugestaoRepository.save(sugestao);
        }).orElseThrow(() -> new RuntimeException("Sugestão não encontrada: ID " + id));
    }
}