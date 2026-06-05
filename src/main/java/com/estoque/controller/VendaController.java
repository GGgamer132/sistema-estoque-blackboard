package com.estoque.controller;

import com.estoque.dto.VendaDTO;
import com.estoque.model.Venda;
import com.estoque.service.EspecialistaLojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private EspecialistaLojaService especialistaLojaService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarVenda(@RequestBody VendaDTO request) {
        try {
            Venda venda = especialistaLojaService.registrarVenda(
                request.getProdutoId(),
                request.getLojaId(),
                request.getQuantidade()
            );
            return ResponseEntity.ok(venda);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}