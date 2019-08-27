package br.com.pcabrantes.prodmanager.controller;

import br.com.pcabrantes.prodmanager.adapter.CategoriaAdapter;
import br.com.pcabrantes.prodmanager.dto.CategoriaDTO;
import br.com.pcabrantes.prodmanager.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaAdapter categoriaAdapter;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaDTO> listar() throws Exception {
        return categoriaService.listar().stream()
                .map(categoria -> categoriaAdapter.toDTO(categoria))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoriaDTO obter(@PathVariable Long id) throws Exception {
        return categoriaAdapter.toDTO(categoriaService.obter(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> cadastrar(@RequestBody CategoriaDTO categoria) throws Exception {
        categoria.setId(null);
        return new ResponseEntity<>(categoriaAdapter.toDTO(categoriaService.salvar(categoriaAdapter.fromDTO(categoria))),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public CategoriaDTO atualizar(@PathVariable Long id, @RequestBody CategoriaDTO categoria) throws Exception {
        categoria.setId(id);
        return categoriaAdapter.toDTO(categoriaService.salvar(categoriaAdapter.fromDTO(categoria)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remover(@PathVariable Long id) throws Exception {
        categoriaService.remover(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
