package com.estoque.service;

import com.estoque.model.Loja;
import com.estoque.repository.LojaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LojaService {

    @Autowired
    private LojaRepository lojaRepository;

    public List<Loja> obterTodas() {
        return lojaRepository.findAll();
    }

    public Optional<Loja> obterPorId(Long id) {
        return lojaRepository.findById(id);
    }

    public Loja salvar(Loja loja) {
        return lojaRepository.save(loja);
    }

    public Loja atualizar(Long id, Loja lojaAtualizada) {
        return lojaRepository.findById(id).map(loja -> {
            loja.setNome(lojaAtualizada.getNome());
            loja.setEndereco(lojaAtualizada.getEndereco());
            return lojaRepository.save(loja);
        }).orElseThrow(() -> new RuntimeException("Loja não encontrada: ID " + id));
    }

    public void deletar(Long id) {
        lojaRepository.deleteById(id);
    }
}