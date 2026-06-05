package com.estoque.service;
import com.estoque.model.Fornecedor;
import com.estoque.model.FornecedorCatalogo;
import com.estoque.model.Produto;
import com.estoque.repository.FornecedorCatalogoRepository;
import com.estoque.repository.FornecedorRepository;
import com.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private FornecedorCatalogoRepository fornecedorCatalogoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Fornecedor> obterTodos() {
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> obterPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

    public Fornecedor salvar(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor atualizar(Long id, Fornecedor atualizado) {
        return fornecedorRepository.findById(id).map(f -> {
            f.setNome(atualizado.getNome());
            f.setContato(atualizado.getContato());
            return fornecedorRepository.save(f);
        }).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado: ID " + id));
    }

    public void deletar(Long id) {
        fornecedorRepository.deleteById(id);
    }


    public List<FornecedorCatalogo> obterCatalogoPorProduto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado: ID " + produtoId));
        return fornecedorCatalogoRepository.findByProduto(produto);
    }


    public FornecedorCatalogo adicionarAoCatalogo(FornecedorCatalogo catalogo) {
        return fornecedorCatalogoRepository.save(catalogo);
    }
}