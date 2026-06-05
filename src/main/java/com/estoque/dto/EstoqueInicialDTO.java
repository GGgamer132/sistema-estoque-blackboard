package com.estoque.dto;

public class EstoqueInicialDTO {
    private Long produtoId;
    private Long lojaId;
    private Integer quantidade;

    public EstoqueInicialDTO() {}

    public EstoqueInicialDTO(Long produtoId, Long lojaId, Integer quantidade) {
        this.produtoId = produtoId;
        this.lojaId = lojaId;
        this.quantidade = quantidade;
    }

    public Long getProdutoId() {
    	return produtoId;
    }
    public void setProdutoId(Long produtoId) {
    	this.produtoId = produtoId; 
    }

    public Long getLojaId() { 
    	return lojaId;
    }
    public void setLojaId(Long lojaId) {
    	this.lojaId = lojaId;
    }

    public Integer getQuantidade() {
    	return quantidade;
    }
    public void setQuantidade(Integer quantidade) { 
    	this.quantidade = quantidade;
    }
}