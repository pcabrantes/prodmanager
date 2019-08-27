package br.com.pcabrantes.prodmanager.repository.test;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import br.com.pcabrantes.prodmanager.repository.ClienteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void whenFindByEmail_thenReturnCliente() {

        Cliente cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setSenha("123");
        cliente.setEmail("teste@teste.com");

        entityManager.persist(cliente);
        entityManager.flush();

        Cliente encontrado = clienteRepository.findByEmail(cliente.getEmail()).orElse(null);

        assertNotNull(encontrado);
        assertEquals(cliente.getEmail(), encontrado.getEmail());
    }

    @Test(expected = EntityExistsException.class)
    public void whenInsertDuplicateEmail_thenThrowException() {
        Cliente cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setSenha("123");
        cliente.setEmail("teste@teste.com");

        Cliente cliente1 = new Cliente();
        cliente1.setNome("Teste");
        cliente1.setSenha("123");
        cliente1.setEmail("teste@teste.com");

        entityManager.persist(cliente);
        entityManager.flush();

        entityManager.persist(cliente1);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertEmptyPassword_thenThrowException() {
        Cliente cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setEmail("teste@teste.com");

        entityManager.persist(cliente);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertEmptyName_thenThrowException() {
        Cliente cliente = new Cliente();
        cliente.setEmail("teste@teste.com");
        cliente.setSenha("123");

        entityManager.persist(cliente);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenInsertEmptyEmail_thenThrowException() {
        Cliente cliente = new Cliente();
        cliente.setSenha("123");
        cliente.setNome("Teste");

        entityManager.persist(cliente);
        entityManager.flush();
    }
}
