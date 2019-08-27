package br.com.pcabrantes.prodmanager.dto;

import java.math.BigDecimal;

public class PedidoItemDTO {

    private ProdutoDTO produto;
    private String produtoDesc;
    private int quantidade;
    private BigDecimal valor;
    private BigDecimal subtotal;

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public String getProdutoDesc() {
        return produtoDesc;
    }

    public void setProdutoDesc(String produtoDesc) {
        this.produtoDesc = produtoDesc;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
