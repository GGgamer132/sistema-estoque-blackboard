package com.estoque.repository;
import com.estoque.model.SugestaoReposicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SugestaoReposicaoRepository extends JpaRepository<SugestaoReposicao, Long> {
    List<SugestaoReposicao> findByStatus(String status);
}