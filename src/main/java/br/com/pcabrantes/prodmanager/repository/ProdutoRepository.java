package br.com.pcabrantes.prodmanager.repository;

import br.com.pcabrantes.prodmanager.entity.Produto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {

}
