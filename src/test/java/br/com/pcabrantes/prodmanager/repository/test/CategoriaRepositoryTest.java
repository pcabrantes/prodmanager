package br.com.pcabrantes.prodmanager.repository.test;

import br.com.pcabrantes.prodmanager.entity.Categoria;
import br.com.pcabrantes.prodmanager.repository.CategoriaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertNullCategoria_thenThrowException() {
        Categoria categoria = new Categoria();

        entityManager.persist(categoria);
        entityManager.flush();
    }

    @Test(expected = EntityExistsException.class)
    public void whenInsertDuplicateCategoria_thenThrowException() {
        Categoria categoria = new Categoria();
        categoria.setCategoria("teste");

        Categoria categoria1 = new Categoria();
        categoria1.setCategoria("teste");

        entityManager.persist(categoria);
        entityManager.flush();

        entityManager.persist(categoria1);
        entityManager.flush();

    }
}
