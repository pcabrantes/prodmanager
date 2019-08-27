package br.com.pcabrantes.prodmanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @NotNull
    private String nome;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String senha;
    private String rua;
    private String cidade;
    private String bairro;
    private String cep;
    private String estado;

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(idCliente, cliente.idCliente) &&
                Objects.equals(nome, cliente.nome) &&
                Objects.equals(email, cliente.email) &&
                Objects.equals(senha, cliente.senha) &&
                Objects.equals(rua, cliente.rua) &&
                Objects.equals(cidade, cliente.cidade) &&
                Objects.equals(bairro, cliente.bairro) &&
                Objects.equals(cep, cliente.cep) &&
                Objects.equals(estado, cliente.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, nome, email, senha, rua, cidade, bairro, cep, estado);
    }
}
