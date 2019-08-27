package br.com.pcabrantes.prodmanager.repository;

import br.com.pcabrantes.prodmanager.entity.Pedido;
import org.springframework.data.repository.CrudRepository;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {

    void deleteAllByCliente_IdCliente(Long idCliente);
}
