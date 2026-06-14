package com.estoque.dto;

public class CompraAvulsoResponseDTO {

    private Long id;
    private String produtoNome;
    private String lojaNome;
    private String fornecedorNome;
    private Integer quantidade;
    private Double precoUnitario;
    private Double totalCusto;
    private Integer novoEstoque;

    public CompraAvulsoResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public String getLojaNome() { return lojaNome; }
    public void setLojaNome(String lojaNome) { this.lojaNome = lojaNome; }

    public String getFornecedorNome() { return fornecedorNome; }
    public void setFornecedorNome(String fornecedorNome) { this.fornecedorNome = fornecedorNome; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(Double precoUnitario) { this.precoUnitario = precoUnitario; }

    public Double getTotalCusto() { return totalCusto; }
    public void setTotalCusto(Double totalCusto) { this.totalCusto = totalCusto; }

    public Integer getNovoEstoque() { return novoEstoque; }
    public void setNovoEstoque(Integer novoEstoque) { this.novoEstoque = novoEstoque; }
}