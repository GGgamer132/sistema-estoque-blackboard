package com.estoque.repository;
import com.estoque.model.Estoque;
import com.estoque.model.Loja;
import com.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    // Busca o estoque de um produto específico em uma loja específica
    Optional<Estoque> findByProdutoAndLoja(Produto produto, Loja loja);

    // Busca todos os estoques de um produto (em todas as lojas)
    List<Estoque> findByProduto(Produto produto);

    // Busca todos os estoques de uma loja
    List<Estoque> findByLoja(Loja loja);
}