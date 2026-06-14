package com.estoque.service;

import com.estoque.model.Venda;
import com.estoque.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    public List<Venda> obterTodas() {
        return vendaRepository.findAll();
    }
}