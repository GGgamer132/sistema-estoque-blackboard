package com.estoque.controller;
import com.estoque.model.Loja;
import com.estoque.service.LojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lojas")
public class LojaController {

    @Autowired
    private LojaService lojaService;

    @GetMapping
    public List<Loja> obterTodas() {
        return lojaService.obterTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loja> obterPorId(@PathVariable Long id) {
        return lojaService.obterPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Loja criar(@RequestBody Loja loja) {
        return lojaService.salvar(loja);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loja> atualizar(
            @PathVariable Long id,
            @RequestBody Loja loja) {
        try {
            return ResponseEntity.ok(lojaService.atualizar(id, loja));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        lojaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}