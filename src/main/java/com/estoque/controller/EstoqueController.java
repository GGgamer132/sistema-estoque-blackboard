package com.estoque.controller;
import com.estoque.dto.EstoqueInicialDTO;
import com.estoque.model.Estoque;
import com.estoque.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public List<Estoque> obterTodos() {
        return estoqueService.obterTodos();
    }

    @GetMapping("/loja/{lojaId}")
    public ResponseEntity<?> obterPorLoja(@PathVariable Long lojaId) {
        try {
            return ResponseEntity.ok(estoqueService.obterPorLoja(lojaId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<?> obterPorProduto(@PathVariable Long produtoId) {
        try {
            return ResponseEntity.ok(estoqueService.obterPorProduto(produtoId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/inicial")
    public ResponseEntity<?> cadastrarEstoqueInicial(@RequestBody EstoqueInicialDTO request) {
        try {
            Estoque estoque = estoqueService.cadastrarEstoqueInicial(
                request.getProdutoId(),
                request.getLojaId(),
                request.getQuantidade()
            );
            return ResponseEntity.ok(estoque);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}