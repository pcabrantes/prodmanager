package br.com.pcabrantes.prodmanager.adapter.test;

import br.com.pcabrantes.prodmanager.adapter.CategoriaAdapter;
import br.com.pcabrantes.prodmanager.adapter.ClienteAdapter;
import br.com.pcabrantes.prodmanager.adapter.PedidoAdapter;
import br.com.pcabrantes.prodmanager.adapter.ProdutoAdapter;
import br.com.pcabrantes.prodmanager.dto.*;
import br.com.pcabrantes.prodmanager.entity.*;
import br.com.pcabrantes.prodmanager.util.enums.StatusPedido;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class PedidoAdapterTest {

    @TestConfiguration
    static class PedidoAdapterTestContextConfiguration {
        @Bean
        public ProdutoAdapter produtoAdapter() {
            return new ProdutoAdapter();
        }

        @Bean
        public ClienteAdapter clienteAdapter() {
            return new ClienteAdapter();
        }

        @Bean
        public PedidoAdapter pedidoAdapter() {
            return new PedidoAdapter();
        }

        @Bean
        public CategoriaAdapter categoriaAdapter() {
            return new CategoriaAdapter();
        }

    }

    @Autowired
    private PedidoAdapter pedidoAdapter;

    @Test
    public void whenPedidoIsNull_ThenReturnNull() {
        PedidoDTO dto = pedidoAdapter.toDTO(null);

        assertNull(dto);
    }

    @Test
    public void whenPedidoDTOIsNull_ThenReturnNull() {
        Pedido pedido = pedidoAdapter.fromDTO(null);

        assertNull(pedido);
    }

    @Test
    public void whenPedidoIsNotNull_ThenReturnPedidoDTO() {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        pedido.setCliente(new Cliente());
        pedido.setSessao("ABC232FFF");
        pedido.setData(LocalDateTime.now());

        Produto produto1 = new Produto();
        produto1.setIdProduto(1L);
        produto1.setPreco(BigDecimal.TEN);
        produto1.setCategoria(new Categoria());
        produto1.setDescricao("descricao");
        produto1.setProduto("produto");
        produto1.setFoto("Foto");
        produto1.setQuantidade(1);

        PedidoItem item1 = new PedidoItem();
        item1.setProduto(produto1);
        item1.setQuantidade(1);
        item1.setSubtotal(BigDecimal.TEN);
        item1.setProdutoDesc("descricao");
        item1.setValor(BigDecimal.TEN);

        List<PedidoItem> itens = new ArrayList<>();
        itens.add(item1);

        pedido.setItens(itens);

        PedidoDTO dto = pedidoAdapter.toDTO(pedido);

        assertEquals(pedido.getIdPedido(), dto.getId());
        assertEquals(pedido.getStatus(), dto.getStatus());
        assertEquals(StatusPedido.getById(pedido.getStatus()).getDescricao(), dto.getStatusDescricao());
        assertEquals(pedido.getData(), dto.getData());
        assertEquals(pedido.getSessao(), dto.getSessao());
        assertEquals(pedido.getItens().size(), dto.getItens().size());

        PedidoItemDTO itemDTO = dto.getItens().get(0);

        assertEquals(item1.getQuantidade(), itemDTO.getQuantidade());
        assertEquals(item1.getProduto().getIdProduto(), itemDTO.getProduto().getId());
        assertEquals(item1.getProdutoDesc(), itemDTO.getProdutoDesc());
        assertEquals(item1.getSubtotal(), itemDTO.getSubtotal());
        assertEquals(item1.getValor(), itemDTO.getValor());

        assertNotNull(dto.getCliente());
    }


    @Test
    public void whenPedidoDTOIsNotNull_ThenReturnPedido() {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(1L);
        dto.setStatus(StatusPedido.EM_PROCESSAMENTO.getId());
        dto.setCliente(new ClienteDTO());
        dto.setSessao("ABC232FFF");
        dto.setData(LocalDateTime.now());

        ProdutoDTO produto1 = new ProdutoDTO();
        produto1.setId(1L);
        produto1.setPreco(BigDecimal.TEN);
        produto1.setCategoria(new CategoriaDTO());
        produto1.setDescricao("descricao");
        produto1.setProduto("produto");
        produto1.setFoto("Foto");
        produto1.setQuantidade(1);

        PedidoItemDTO item1 = new PedidoItemDTO();
        item1.setProduto(produto1);
        item1.setQuantidade(1);
        item1.setSubtotal(BigDecimal.TEN);
        item1.setProdutoDesc("descricao");
        item1.setValor(BigDecimal.TEN);

        List<PedidoItemDTO> itens = new ArrayList<>();
        itens.add(item1);

        dto.setItens(itens);

        Pedido pedido = pedidoAdapter.fromDTO(dto);

        assertEquals(dto.getId(), pedido.getIdPedido());
        assertEquals(dto.getStatus(), pedido.getStatus());
        assertEquals(dto.getData(), pedido.getData());
        assertEquals(dto.getSessao(), pedido.getSessao());
        assertEquals(dto.getItens().size(), pedido.getItens().size());

        PedidoItem item = pedido.getItens().get(0);

        assertEquals(item1.getQuantidade(), item.getQuantidade());
        assertEquals(item1.getProduto().getId(), item.getProduto().getIdProduto());
        assertEquals(item1.getProdutoDesc(), item.getProdutoDesc());
        assertEquals(item1.getSubtotal(), item.getSubtotal());
        assertEquals(item1.getValor(), item.getValor());

        assertNotNull(pedido.getCliente());
    }
}
