package br.com.pcabrantes.prodmanager.repository;

import br.com.pcabrantes.prodmanager.entity.PedidoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PedidoItemRepository extends CrudRepository<PedidoItem, Long> {

    List<PedidoItem> findAllByPedido_IdPedido(Long idPedido);

    void deleteAllByPedido_IdPedido(Long idPedido);
    void deleteAllByPedido_Cliente_IdCliente(Long idCliente);
}