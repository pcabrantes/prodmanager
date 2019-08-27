package br.com.pcabrantes.prodmanager.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;

    @NotNull
    private String produto;

    @NotNull
    @Min(0)
    private BigDecimal preco;

    @NotNull
    @Min(0)
    private int quantidade;
    private String descricao;
    private String foto;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoriaId")
    private Categoria categoria;

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto1 = (Produto) o;
        return quantidade == produto1.quantidade &&
                Objects.equals(idProduto, produto1.idProduto) &&
                Objects.equals(produto, produto1.produto) &&
                Objects.equals(preco, produto1.preco) &&
                Objects.equals(descricao, produto1.descricao) &&
                Objects.equals(foto, produto1.foto) &&
                Objects.equals(categoria, produto1.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, produto, preco, quantidade, descricao, foto, categoria);
    }
}
