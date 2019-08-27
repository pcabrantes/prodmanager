package br.com.pcabrantes.prodmanager.controller;

import br.com.pcabrantes.prodmanager.adapter.ClienteAdapter;
import br.com.pcabrantes.prodmanager.dto.ClienteDTO;
import br.com.pcabrantes.prodmanager.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteAdapter clienteAdapter;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> listar() throws Exception {
        return clienteService.listar().stream()
                .map(cliente -> clienteAdapter.toDTO(cliente))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteDTO obter(@PathVariable Long id) throws Exception {
        return clienteAdapter.toDTO(clienteService.obter(id));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> cadastrar(@RequestBody ClienteDTO cliente) throws Exception {
        cliente.setId(null);
        return new ResponseEntity<>(clienteAdapter.toDTO(clienteService.salvar(clienteAdapter.fromDTO(cliente)))
                , HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ClienteDTO atualizar(@PathVariable Long id, @RequestBody ClienteDTO cliente) throws Exception {
        cliente.setId(id);
        return clienteAdapter.toDTO(clienteService.salvar(clienteAdapter.fromDTO(cliente)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remover(@PathVariable Long id) throws Exception {
        clienteService.remover(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
