package br.com.pcabrantes.prodmanager.service.impl;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import br.com.pcabrantes.prodmanager.entity.Pedido;
import br.com.pcabrantes.prodmanager.entity.PedidoItem;
import br.com.pcabrantes.prodmanager.entity.Produto;
import br.com.pcabrantes.prodmanager.exception.*;
import br.com.pcabrantes.prodmanager.repository.PedidoItemRepository;
import br.com.pcabrantes.prodmanager.repository.PedidoRepository;
import br.com.pcabrantes.prodmanager.service.ClienteService;
import br.com.pcabrantes.prodmanager.service.PedidoService;
import br.com.pcabrantes.prodmanager.service.ProdutoService;
import br.com.pcabrantes.prodmanager.util.enums.StatusPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PedidoServiceImpl implements PedidoService {

    private static Logger LOGGER = LoggerFactory.getLogger(PedidoServiceImpl.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoItemRepository pedidoItemRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ClienteService clienteService;

    @Override
    public Pedido obter(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException {
        Pedido pedido = null;

        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id deve ser informado");
            }

            pedido = pedidoRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id));
            pedido.setItens(pedidoItemRepository.findAllByPedido_IdPedido(pedido.getIdPedido()));
        } catch (RegistroNaoEncontradoException | ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return pedido;
    }

    @Override
    public List<Pedido> listar() throws ErroInesperadoException {
        List<Pedido> pedidos = null;

        try {
            pedidos = StreamSupport.stream(pedidoRepository.findAll().spliterator(), false)
                    .map(pedido -> {pedido.setItens(pedidoItemRepository.findAllByPedido_IdPedido(pedido.getIdPedido())); return pedido;})
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return pedidos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pedido salvar(Pedido pedido) throws RegistroNaoEncontradoException,
            ErroInesperadoException, AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Pedido pedidoDB = null;
        List<PedidoItem> pedidoItemsDB = null;

        try {
            if (pedido == null) {
                throw new ParametroInvalidoException("O pedido deve ser informado");
            }

            if (pedido.getCliente() == null || pedido.getCliente().getIdCliente() == null) {
                throw new ParametrosInsuficientesException("Cliente");
            }

            if (!CollectionUtils.isEmpty(pedido.getItens())) {

                long countInvalidos = pedido.getItens().parallelStream().filter(
                        pedidoItem -> pedidoItem == null || pedidoItem.getProduto() == null
                                || pedidoItem.getProduto().getIdProduto() == null).count();

                if (countInvalidos > 0) {
                    throw new ParametrosInsuficientesException("PedidoItem/Produto");
                }
            }

            if (pedido.getIdPedido() != null) {
                pedidoDB = this.obter(pedido.getIdPedido());

                verificaAtualizacaoStatus(pedidoDB.getStatus(), pedido.getStatus());

                if (!pedido.getCliente().getIdCliente().equals(pedidoDB.getCliente().getIdCliente())) {
                    throw new AlteracaoNaoPermitidaException("Não é permitido alterar o cliente de um pedido");
                }

                pedidoItemsDB = pedidoItemRepository.findAllByPedido_IdPedido(pedido.getIdPedido());

                pedidoDB.setData(pedido.getData());
                pedidoDB.setSessao(pedido.getSessao());
                pedidoDB.setStatus(pedido.getStatus());

                pedidoItemRepository.deleteAllByPedido_IdPedido(pedidoDB.getIdPedido());

            } else {
                pedidoDB = pedido;

                Cliente cliente = clienteService.obter(pedidoDB.getCliente().getIdCliente());

                pedidoDB.setCliente(cliente);
                pedidoDB.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
            }

            if (!CollectionUtils.isEmpty(pedido.getItens())) {
                pedido.setItens(merge(pedido.getItens()));

                pedido.setItens(
                        pedido.getItens().stream()
                                .filter(pedidoItem -> pedidoItem.getQuantidade() > 0)
                                .collect(Collectors.toList())
                );
            }

            if (CollectionUtils.isEmpty(pedido.getItens())) {
                throw new AlteracaoNaoPermitidaException("O pedido não contém itens");
            }

            if (pedidoDB.getData() == null) {
                pedidoDB.setData(LocalDateTime.now());
            }

            pedidoRepository.save(pedido);

            List<PedidoItem> pedidoItems = new ArrayList<>();

            for (PedidoItem item : pedido.getItens()) {
                item.setPedido(pedido);
                pedidoItems.add(salvarItem(item, pedidoItemsDB));
            }

            pedidoDB.setItens(pedidoItems);
        } catch (RegistroNaoEncontradoException | AlteracaoNaoPermitidaException | ParametroInvalidoException | ParametrosInsuficientesException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return pedidoDB;
    }

    @Override
    public Pedido atualizarStatus(Long idPedido, int novoStatus)
            throws RegistroNaoEncontradoException, ErroInesperadoException, AlteracaoNaoPermitidaException {
        Pedido pedido = null;

        try {
            pedido = this.obter(idPedido);

            if (pedido.getStatus() != novoStatus) {
                verificaAtualizacaoStatus(pedido.getStatus(), novoStatus);

                pedido.setStatus(novoStatus);
                pedidoRepository.save(pedido);
            }
        } catch (RegistroNaoEncontradoException | AlteracaoNaoPermitidaException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return pedido;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remover(Long id) throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException, ExclusaoNaoPermitidaException {
        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id deve ser informado");
            }
            pedidoItemRepository.deleteAllByPedido_IdPedido(id);
            pedidoRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ExclusaoNaoPermitidaException();
        } catch (ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RegistroNaoEncontradoException(id);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }
    }

    @Override
    public void removerPorCliente(Long idCliente) throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException {
        try {
            if (idCliente == null) {
                throw new ParametroInvalidoException("O id deve ser informado");
            }
            pedidoItemRepository.deleteAllByPedido_Cliente_IdCliente(idCliente);
            pedidoRepository.deleteAllByCliente_IdCliente(idCliente);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ExclusaoNaoPermitidaException();
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RegistroNaoEncontradoException(idCliente);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }
    }

    private PedidoItem salvarItem(PedidoItem pedidoItem, List<PedidoItem> pedidoItems) throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametrosInsuficientesException, ParametroInvalidoException {

        if (pedidoItem.getProduto() == null) {
            throw new ParametrosInsuficientesException("Produto");
        }

        Produto produto = produtoService.obter(pedidoItem.getProduto().getIdProduto());
        pedidoItem.setProduto(produto);

        int saldoQtd = -pedidoItem.getQuantidade();

        if (pedidoItems != null) {
            List<PedidoItem> itens = pedidoItems.stream().filter(pItem -> pItem.getProduto().getIdProduto().equals(
                    pedidoItem.getProduto().getIdProduto())).collect(Collectors.toList());

            if (!itens.isEmpty()) {
                saldoQtd = itens.get(0).getQuantidade() - pedidoItem.getQuantidade();
            }
        }

        int qtd = produto.getQuantidade()+saldoQtd;

        if (pedidoItem.getQuantidade() == 0) {
            throw new ParametroInvalidoException("Não é possível incluir um item com quantidade 0");
        } else if (qtd < 0) {
            throw new ParametroInvalidoException("Não existem itens [" + produto.getProduto() + "] suficientes para este pedido");
        }

        pedidoItem.setProdutoDesc(produto.getProduto());
        pedidoItem.setSubtotal(produto.getPreco().multiply(new BigDecimal(pedidoItem.getQuantidade())));
        pedidoItem.setValor(produto.getPreco());

        pedidoItemRepository.save(pedidoItem);

        produto.setQuantidade(qtd);
        produtoService.salvar(produto);

        return pedidoItem;
    }

    private List<PedidoItem> merge(List<PedidoItem> pedidoItems) {

        List<PedidoItem> merged = new ArrayList<>();
        List<PedidoItem> remove = null;
        List<Produto> produtos = pedidoItems.stream().map(pedidoItem -> pedidoItem.getProduto()).collect(Collectors.toList());

        for (Produto p : produtos) {
            if (pedidoItems.size() == 0) {
                break;
            }
            remove = pedidoItems.stream().filter(pedidoItem -> pedidoItem.getProduto().getIdProduto().equals(p.getIdProduto())).collect(Collectors.toList());

            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProduto(p);

            for (PedidoItem item : remove) {
                pedidoItem.setQuantidade(pedidoItem.getQuantidade()+item.getQuantidade());
            }

            merged.add(pedidoItem);
            pedidoItems.removeAll(remove);
        }

        return merged;
    }

    private void verificaAtualizacaoStatus(int statusInicial, int statusFinal) throws AlteracaoNaoPermitidaException {

        if (statusFinal < 0 || statusFinal > 2) {
            throw new AlteracaoNaoPermitidaException("Status inexistente");
        } else if (statusInicial == StatusPedido.CONCLUIDO.getId() || statusInicial == StatusPedido.CANCELADO.getId()) {
            throw new AlteracaoNaoPermitidaException("Status concluído ou cancelado");
        }
    }

}
