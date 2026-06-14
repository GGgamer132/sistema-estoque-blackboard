package com.estoque.dto;

public class CompraAvulsoRequestDTO {

    private Long produtoId;
    private Long lojaId;
    private Integer quantidade;

    public CompraAvulsoRequestDTO() {}

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public Long getLojaId() { return lojaId; }
    public void setLojaId(Long lojaId) { this.lojaId = lojaId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}