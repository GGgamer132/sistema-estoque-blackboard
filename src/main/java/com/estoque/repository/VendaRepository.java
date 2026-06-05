package com.estoque.repository;
import com.estoque.model.Venda;
import com.estoque.model.Loja;
import com.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
	// Busca todas as vendas de uma loja (de qualquer produto)
    List<Venda> findByLoja(Loja loja);
    
    // Busca todas as vendas de um produto (em todas as lojas)
    List<Venda> findByProduto(Produto produto);
}