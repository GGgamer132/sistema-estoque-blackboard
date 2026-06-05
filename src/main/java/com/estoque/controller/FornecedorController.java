package com.estoque.controller;
import com.estoque.model.Fornecedor;
import com.estoque.model.FornecedorCatalogo;
import com.estoque.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public List<Fornecedor> obterTodos() {
        return fornecedorService.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> obterPorId(@PathVariable Long id) {
        return fornecedorService.obterPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Fornecedor criar(@RequestBody Fornecedor fornecedor) {
        return fornecedorService.salvar(fornecedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Fornecedor fornecedor) {
        try {
            return ResponseEntity.ok(fornecedorService.atualizar(id, fornecedor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/catalogo/produto/{produtoId}")
    public ResponseEntity<?> obterCatalogoPorProduto(@PathVariable Long produtoId) {
        try {
            return ResponseEntity.ok(fornecedorService.obterCatalogoPorProduto(produtoId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/catalogo")
    public FornecedorCatalogo adicionarAoCatalogo(@RequestBody FornecedorCatalogo catalogo) {
        return fornecedorService.adicionarAoCatalogo(catalogo);
    }
}