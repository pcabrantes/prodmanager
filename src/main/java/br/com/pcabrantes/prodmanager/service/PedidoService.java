package br.com.pcabrantes.prodmanager.service;

import br.com.pcabrantes.prodmanager.entity.Pedido;
import br.com.pcabrantes.prodmanager.exception.*;

import java.util.List;

public interface PedidoService {

    Pedido obter(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException;
    List<Pedido> listar() throws ErroInesperadoException;
    Pedido salvar(Pedido pedido) throws RegistroNaoEncontradoException, ErroInesperadoException, AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException;
    void remover(Long id) throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException, ExclusaoNaoPermitidaException;
    void removerPorCliente(Long idCliente) throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException;
    Pedido atualizarStatus(Long idPedido, int novoStatus) throws RegistroNaoEncontradoException, ErroInesperadoException, AlteracaoNaoPermitidaException;
}
