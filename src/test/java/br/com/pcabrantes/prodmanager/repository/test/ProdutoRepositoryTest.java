package br.com.pcabrantes.prodmanager.repository.test;

import br.com.pcabrantes.prodmanager.entity.Categoria;
import br.com.pcabrantes.prodmanager.entity.Produto;
import br.com.pcabrantes.prodmanager.repository.ProdutoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProdutoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Categoria categoria;

    @Before
    public void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setCategoria("Categoria");
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertProdutoWithNullProduto_thenThrowException() {
        Produto produto = new Produto();
        produto.setQuantidade(1);
        produto.setPreco(BigDecimal.TEN);
        produto.setCategoria(categoria);

        entityManager.persist(produto);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertProdutoWithNullPreco_thenThrowException() {
        Produto produto = new Produto();
        produto.setProduto("Produto");
        produto.setQuantidade(1);
        produto.setCategoria(categoria);

        entityManager.persist(produto);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertProdutoWithPrecoLessThanZero_thenThrowException() {
        Produto produto = new Produto();
        produto.setProduto("Produto");
        produto.setQuantidade(1);
        produto.setPreco(BigDecimal.valueOf(-1));
        produto.setCategoria(categoria);

        entityManager.persist(produto);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertProdutoWithQuantidadeLessThanZero_thenThrowException() {
        Produto produto = new Produto();
        produto.setProduto("Produto");
        produto.setQuantidade(-1);
        produto.setPreco(BigDecimal.valueOf(1));
        produto.setCategoria(categoria);

        entityManager.persist(produto);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertProdutoWithNullCategoria_thenThrowException() {
        Produto produto = new Produto();
        produto.setProduto("Produto");
        produto.setQuantidade(1);
        produto.setPreco(BigDecimal.valueOf(1));

        entityManager.persist(produto);
        entityManager.flush();
    }
}
