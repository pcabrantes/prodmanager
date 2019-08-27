package br.com.pcabrantes.prodmanager.controller;

import br.com.pcabrantes.prodmanager.adapter.ProdutoAdapter;
import br.com.pcabrantes.prodmanager.dto.ProdutoDTO;
import br.com.pcabrantes.prodmanager.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoAdapter produtoAdapter;

	@Autowired
	private ProdutoService produtoService;

	@GetMapping
	public List<ProdutoDTO> listar() throws Exception {
		return produtoService.listar().stream().map(produto -> produtoAdapter.toDTO(produto)).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ProdutoDTO obter(@PathVariable Long id) throws Exception {
		return produtoAdapter.toDTO(produtoService.obter(id));
	}

	@PostMapping
	public ResponseEntity<ProdutoDTO> cadastrar(@RequestBody ProdutoDTO produto) throws Exception {
		return new ResponseEntity<>(produtoAdapter.toDTO(produtoService.salvar(produtoAdapter.fromDTO(produto))),
				HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ProdutoDTO atualizar(@PathVariable Long id, @RequestBody ProdutoDTO produto) throws Exception {
		produto.setId(id);
		return produtoAdapter.toDTO(produtoService.salvar(produtoAdapter.fromDTO(produto)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity remover(@PathVariable Long id) throws Exception {
		produtoService.remover(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
