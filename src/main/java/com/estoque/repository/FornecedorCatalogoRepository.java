package com.estoque.repository;
import com.estoque.model.FornecedorCatalogo;
import com.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FornecedorCatalogoRepository extends JpaRepository<FornecedorCatalogo, Long> {
    // Busca todos os fornecedores que vendem um produto específico
    List<FornecedorCatalogo> findByProduto(Produto produto);
}