package br.com.pcabrantes.prodmanager.service.test;

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
import br.com.pcabrantes.prodmanager.service.impl.PedidoServiceImpl;
import br.com.pcabrantes.prodmanager.util.enums.StatusPedido;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PedidoServiceTest {

    @TestConfiguration
    static class PedidoServiceImplTestContextConfiguration {

        @Bean
        public PedidoService pedidoService() {
            return new PedidoServiceImpl();
        }
    }

    @Autowired
    private PedidoService pedidoService;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private PedidoItemRepository pedidoItemRepository;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private ClienteService clienteService;

    @Before
    public void setUp() throws ErroInesperadoException, ParametroInvalidoException, RegistroNaoEncontradoException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Produto produto = new Produto();
        produto.setIdProduto(1L);
        produto.setQuantidade(10);
        produto.setPreco(BigDecimal.TEN);

        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setProduto(produto);
        pedidoItem.setQuantidade(2);

        List<PedidoItem> items = new ArrayList<>();
        items.add(pedidoItem);

        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());
        pedido.setItens(items);

        Pedido pedido2 = new Pedido();
        pedido2.setIdPedido(2L);
        pedido2.setCliente(cliente);
        pedido2.setStatus(StatusPedido.CANCELADO.getId());
        pedido2.setData(LocalDateTime.now());

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.findById(2L)).thenReturn(Optional.of(pedido2));

        when(produtoService.obter(1L)).thenReturn(produto);

        when(pedidoRepository.findById(longThat(argument -> argument != null && argument > 2))).thenAnswer(invocation -> {
            Pedido pedido1 = new Pedido();
            pedido1.setIdPedido((Long) invocation.getArguments()[0]);
            return Optional.of(pedido1);
        });

        when(pedidoRepository.findById(longThat(argument -> argument != null && argument < 0))).thenReturn(Optional.empty());

        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(new Pedido(), new Pedido()));

        when(pedidoRepository.save(argThat(argument -> argument != null && argument.getIdPedido() == null))).thenAnswer(invocation -> {
            Pedido pedido1 = (Pedido) invocation.getArguments()[0];
            pedido1.setIdPedido(new Random().nextLong());
            return pedido1;
        });

        when(pedidoRepository.save(argThat(argument -> argument != null && argument.getIdPedido() != null))).thenAnswer(invocation -> {
            Pedido pedido1 = (Pedido) invocation.getArguments()[0];
            return pedido1;
        });

        when(pedidoItemRepository.findAllByPedido_IdPedido(1L)).thenReturn(items);

        doThrow(EmptyResultDataAccessException.class)
                .when(pedidoRepository).deleteById(-1L);

        doThrow(DataIntegrityViolationException.class)
                .when(pedidoRepository).deleteById(20L);

        doThrow(EmptyResultDataAccessException.class)
                .when(pedidoRepository).deleteAllByCliente_IdCliente(-1L);

        doThrow(DataIntegrityViolationException.class)
                .when(pedidoRepository).deleteAllByCliente_IdCliente(20L);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenGettNull_thenThrowExceptionInvalid() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        pedidoService.obter(null);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenGetNonExistant_thenThrowExceptionNotFound() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        pedidoService.obter(-1L);
    }

    @Test
    public void whenGetExistant_thenReturnCliente() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        Pedido pedido = pedidoService.obter(1L);

        assertEquals(1L, pedido.getIdPedido().longValue());
    }

    @Test
    public void whenList_thenReturnList() throws ErroInesperadoException {
        List<Pedido> pedidos = pedidoService.listar();

        assertEquals(2, pedidos.size());
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenInsertNull_thenThrowExceptionInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {
        pedidoService.salvar(null);
    }

    @Test(expected = ParametrosInsuficientesException.class)
    public void whenInsertNullCliente_thenThrowExceptionInsuficient() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Pedido pedido = new Pedido();

        pedidoService.salvar(pedido);
    }

    @Test(expected = AlteracaoNaoPermitidaException.class)
    public void whenInsertNoItems_thenThrowExceptionNotPermited() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        pedidoService.salvar(pedido);
    }

    @Test(expected = AlteracaoNaoPermitidaException.class)
    public void whenInsertItemLessThanZero_thenThrowExceptionInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        Produto produto = new Produto();
        produto.setIdProduto(1L);

        PedidoItem item = new PedidoItem();
        item.setProduto(produto);
        item.setQuantidade(-1);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);

        pedido.setItens(items);

        pedidoService.salvar(pedido);
    }

    @Test(expected = ParametrosInsuficientesException.class)
    public void whenInsertItemWithoutProduto_thenThrowExceptionInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        PedidoItem item = new PedidoItem();
        item.setQuantidade(0);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);

        pedido.setItens(items);

        pedidoService.salvar(pedido);
    }

    @Test
    public void whenInsertOk_thenReturnPedido() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        Produto produto = new Produto();
        produto.setIdProduto(1L);

        PedidoItem item = new PedidoItem();
        item.setProduto(produto);
        item.setQuantidade(1);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);

        pedido.setItens(items);

        pedidoService.salvar(pedido);

        assertNotNull(pedido.getIdPedido());
        assertEquals(StatusPedido.EM_PROCESSAMENTO.getId(), pedido.getStatus());
    }

    @Test
    public void whenInsertMerge_thenReturnPedidoMergedItems() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        Produto produto = new Produto();
        produto.setIdProduto(1L);

        PedidoItem item = new PedidoItem();
        item.setProduto(produto);
        item.setQuantidade(1);

        PedidoItem item2 = new PedidoItem();
        item2.setProduto(produto);
        item2.setQuantidade(2);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

        pedido.setItens(items);

        pedidoService.salvar(pedido);

        assertNotNull(pedido.getIdPedido());
        assertEquals(1, pedido.getItens().size());
        assertEquals(3, pedido.getItens().get(0).getQuantidade());
        assertEquals(StatusPedido.EM_PROCESSAMENTO.getId(), pedido.getStatus());
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenInsertMoreThanStock_thenThrowExceptionInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setData(LocalDateTime.now());

        Produto produto = new Produto();
        produto.setIdProduto(1L);

        PedidoItem item = new PedidoItem();
        item.setProduto(produto);
        item.setQuantidade(11);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);

        pedido.setItens(items);

        pedidoService.salvar(pedido);

        assertNotNull(pedido.getIdPedido());
        assertEquals(StatusPedido.EM_PROCESSAMENTO.getId(), pedido.getStatus());
    }

    @Test
    public void whenUpdateOk_thenReturnPedido() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        Produto produto = new Produto();
        produto.setIdProduto(1L);

        PedidoItem item = new PedidoItem();
        item.setProduto(produto);
        item.setQuantidade(1);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);

        pedido.setItens(items);

        pedidoService.salvar(pedido);

        assertEquals(1L, pedido.getIdPedido().longValue());
    }

    @Test(expected = AlteracaoNaoPermitidaException.class)
    public void whenUpdateCancelled_thenThrowExceptionNotPermited() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Pedido pedido = new Pedido();
        pedido.setIdPedido(2L);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        Produto produto = new Produto();
        produto.setIdProduto(1L);

        PedidoItem item = new PedidoItem();
        item.setProduto(produto);
        item.setQuantidade(1);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);

        pedido.setItens(items);

        pedidoService.salvar(pedido);

        assertEquals(1L, pedido.getIdPedido().longValue());
    }

    @Test(expected = AlteracaoNaoPermitidaException.class)
    public void whenUpdateCliente_thenThrowExceptionNotPermited() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(2L);

        Pedido pedido = new Pedido();
        pedido.setIdPedido(2L);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setData(LocalDateTime.now());

        Produto produto = new Produto();
        produto.setIdProduto(1L);

        PedidoItem item = new PedidoItem();
        item.setProduto(produto);
        item.setQuantidade(1);

        List<PedidoItem> items = new ArrayList<>();
        items.add(item);

        pedido.setItens(items);

        pedidoService.salvar(pedido);

        assertEquals(1L, pedido.getIdPedido().longValue());
    }

    @Test(expected = AlteracaoNaoPermitidaException.class)
    public void whenUpdateStatusCancelled_thenThrowExceptionNotPermited() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Pedido pedido = pedidoService.atualizarStatus(2L, StatusPedido.CONCLUIDO.getId());
    }

    @Test
    public void whenUpdateStatus_thenReturnPedido() throws ErroInesperadoException, RegistroNaoEncontradoException,
            AlteracaoNaoPermitidaException, ParametroInvalidoException, ParametrosInsuficientesException {

        Pedido pedido = pedidoService.atualizarStatus(1L, StatusPedido.CONCLUIDO.getId());

        assertEquals(StatusPedido.CONCLUIDO.getId(), pedido.getStatus());
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenRemoveNull_thenThrowExceptionInvalid() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        pedidoService.remover(null);
    }

    @Test(expected = ExclusaoNaoPermitidaException.class)
    public void whenRemoveDependent_thenThrowExceptionNotPermited() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        pedidoService.remover(20L);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenRemoveNotFound_thenThrowExceptionNotFound() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        pedidoService.remover(-1L);
    }

    @Test
    public void whenRemoveOk_thenDoNothing() throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException, ExclusaoNaoPermitidaException {
        pedidoService.remover(2L);
    }

    @Test(expected = ExclusaoNaoPermitidaException.class)
    public void whenRemoveByClienteDependent_thenThrowExceptionNotPermited() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        pedidoService.removerPorCliente(20L);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenRemoveByClienteNotFound_thenThrowExceptionNotFound() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        pedidoService.removerPorCliente(-1L);
    }

    @Test
    public void whenRemoveByClienteOk_thenDoNothing() throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException, ExclusaoNaoPermitidaException {
        pedidoService.remover(2L);
    }

}
