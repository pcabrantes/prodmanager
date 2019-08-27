package br.com.pcabrantes.prodmanager.controller;

import br.com.pcabrantes.prodmanager.adapter.PedidoAdapter;
import br.com.pcabrantes.prodmanager.dto.PedidoDTO;
import br.com.pcabrantes.prodmanager.entity.Pedido;
import br.com.pcabrantes.prodmanager.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoAdapter pedidoAdapter;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<PedidoDTO> listar() throws Exception {
        return pedidoService.listar().stream().map(pedido -> pedidoAdapter.toDTO(pedido)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PedidoDTO obter(@PathVariable Long id) throws Exception {
        return pedidoAdapter.toDTO(pedidoService.obter(id));
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> cadastrar(@RequestBody PedidoDTO pedido) throws Exception {
        pedido.setId(null);
        return new ResponseEntity<>(pedidoAdapter.toDTO(pedidoService.salvar(pedidoAdapter.fromDTO(pedido))),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public PedidoDTO atualizar(@PathVariable Long id, @RequestBody PedidoDTO pedido) throws Exception {
        pedido.setId(id);
        return pedidoAdapter.toDTO(pedidoService.salvar(pedidoAdapter.fromDTO(pedido)));
    }

    @PutMapping("/{id}/status/{novoStatus}")
    public ResponseEntity<PedidoDTO> atualizarStatus(@PathVariable Long id, @PathVariable int novoStatus) throws Exception {
        int statusInicial = pedidoService.obter(id).getStatus();
        Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, novoStatus);

        if (statusInicial == pedidoAtualizado.getStatus()) {
            return new ResponseEntity(pedidoAdapter.toDTO(pedidoAtualizado), HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity(pedidoAdapter.toDTO(pedidoAtualizado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remover(@PathVariable Long id) throws Exception {
        pedidoService.remover(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
