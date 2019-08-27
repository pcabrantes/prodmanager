package br.com.pcabrantes.prodmanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @NotNull
    @Column(unique = true)
    private String categoria;

    public Categoria() {}

    public Categoria(Long id, String categoria) {
        this.idCategoria = id;
        this.categoria = categoria;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria1 = (Categoria) o;
        return Objects.equals(idCategoria, categoria1.idCategoria) &&
                Objects.equals(categoria, categoria1.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoria, categoria);
    }
}
