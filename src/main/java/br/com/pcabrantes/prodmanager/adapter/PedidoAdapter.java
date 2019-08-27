package br.com.pcabrantes.prodmanager.adapter;

import br.com.pcabrantes.prodmanager.dto.PedidoDTO;
import br.com.pcabrantes.prodmanager.dto.PedidoItemDTO;
import br.com.pcabrantes.prodmanager.entity.Pedido;
import br.com.pcabrantes.prodmanager.entity.PedidoItem;
import br.com.pcabrantes.prodmanager.util.enums.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoAdapter {

    @Autowired
    private ClienteAdapter clienteAdapter;

    @Autowired
    private ProdutoAdapter produtoAdapter;

    public PedidoDTO toDTO(Pedido pedido) {

        PedidoDTO dto = null;

        if (pedido != null) {
            dto = new PedidoDTO();
            dto.setId(pedido.getIdPedido());
            dto.setData(pedido.getData());
            dto.setSessao(pedido.getSessao());
            dto.setStatus(pedido.getStatus());
            dto.setStatusDescricao(StatusPedido.getById(pedido.getStatus()).getDescricao());

            dto.setCliente(clienteAdapter.toDTO(pedido.getCliente()));

            if (!CollectionUtils.isEmpty(pedido.getItens())) {

                PedidoItemDTO pedidoItemDTO = null;
                List<PedidoItemDTO> itens = new ArrayList<>();

                for (PedidoItem item : pedido.getItens()) {
                    pedidoItemDTO = new PedidoItemDTO();
                    pedidoItemDTO.setProdutoDesc(item.getProdutoDesc());
                    pedidoItemDTO.setQuantidade(item.getQuantidade());
                    pedidoItemDTO.setSubtotal(item.getSubtotal());
                    pedidoItemDTO.setValor(item.getValor());
                    pedidoItemDTO.setProduto(produtoAdapter.toDTO(item.getProduto()));
                    itens.add(pedidoItemDTO);
                }

                dto.setItens(itens);
            }
        }

        return dto;
    }

    public Pedido fromDTO(PedidoDTO dto) {

        Pedido pedido = null;

        if (dto != null) {
            pedido = new Pedido();
            pedido.setIdPedido(dto.getId());
            pedido.setData(dto.getData());
            pedido.setSessao(dto.getSessao());
            pedido.setStatus(dto.getStatus());

            pedido.setCliente(clienteAdapter.fromDTO(dto.getCliente()));

            if (!CollectionUtils.isEmpty(dto.getItens())) {

                PedidoItem pedidoItem = null;
                List<PedidoItem> itens = new ArrayList<>();

                for (PedidoItemDTO pedidoItemDTO : dto.getItens()) {
                    pedidoItem = new PedidoItem();
                    pedidoItem.setValor(pedidoItemDTO.getValor());
                    pedidoItem.setSubtotal(pedidoItemDTO.getSubtotal());
                    pedidoItem.setQuantidade(pedidoItemDTO.getQuantidade());
                    pedidoItem.setProdutoDesc(pedidoItemDTO.getProdutoDesc());
                    pedidoItem.setProduto(produtoAdapter.fromDTO(pedidoItemDTO.getProduto()));

                    itens.add(pedidoItem);
                }

                pedido.setItens(itens);

            }
        }

        return pedido;
    }
}
