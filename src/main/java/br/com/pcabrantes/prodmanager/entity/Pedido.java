package br.com.pcabrantes.prodmanager.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    private LocalDateTime data;
    private int status;
    private String sessao;

    @ManyToOne
    @JoinColumn(name = "clienteId")
    private Cliente cliente;

    @Transient
    private List<PedidoItem> itens;

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSessao() {
        return sessao;
    }

    public void setSessao(String sessao) {
        this.sessao = sessao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public List<PedidoItem> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItem> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return status == pedido.status &&
                Objects.equals(idPedido, pedido.idPedido) &&
                Objects.equals(data, pedido.data) &&
                Objects.equals(sessao, pedido.sessao) &&
                Objects.equals(cliente, pedido.cliente) &&
                Objects.equals(itens, pedido.itens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido, data, status, sessao, cliente, itens);
    }
}
