package br.com.pcabrantes.prodmanager.service.test;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import br.com.pcabrantes.prodmanager.exception.*;
import br.com.pcabrantes.prodmanager.repository.ClienteRepository;
import br.com.pcabrantes.prodmanager.service.ClienteService;
import br.com.pcabrantes.prodmanager.service.PedidoService;
import br.com.pcabrantes.prodmanager.service.impl.ClienteServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ClienteServiceTest {

    @TestConfiguration
    static class ClienteServiceImplTestContextConfiguration {

        @Bean
        public ClienteService clienteService() {
            return new ClienteServiceImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
    }

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private ClienteRepository clienteRepository;

    @Before
    public void setUp() {

        String pss = passwordEncoder.encode("admin");

        when(clienteRepository.findById(longThat(argument -> argument != null && argument > 0L))).thenAnswer(invocation -> {
            Cliente cliente = new Cliente();
            cliente.setIdCliente((Long) invocation.getArguments()[0]);
            return Optional.of(cliente);
        });

        when(clienteRepository.findById(longThat(argument -> argument != null && argument < 0L))).thenReturn(Optional.empty());

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(new Cliente(), new Cliente()));

        when(clienteRepository.findByEmail("duplicado@teste.com")).thenAnswer(invocation -> {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(100L);
            cliente.setEmail("duplicado@teste.com");
            return Optional.of(cliente);
        });

        when(clienteRepository.save(argThat(argument -> argument != null && argument.getIdCliente() == null))).thenAnswer(invocation -> {
            Cliente cliente = (Cliente) invocation.getArguments()[0];
            cliente.setIdCliente(new Random().nextLong());
            return cliente;
        });

        when(clienteRepository.save(argThat(argument -> argument != null && argument.getIdCliente() != null))).thenAnswer(invocation -> {
            Cliente cliente = (Cliente) invocation.getArguments()[0];
            return cliente;
        });

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setEmail("teste@teste.com");
        cliente.setSenha("1234");
        cliente.setNome("teste");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        doThrow(EmptyResultDataAccessException.class).when(clienteRepository).deleteById(-1L);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenGettNull_thenThrowExceptionInvalid() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        clienteService.obter(null);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenGetNonExistant_thenThrowExceptionNotFound() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        clienteService.obter(-1L);
    }

    @Test
    public void whenGetExistant_thenReturnCliente() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        Cliente cliente = clienteService.obter(1L);

        assertEquals(1L, cliente.getIdCliente().longValue());
    }

    @Test
    public void whenList_thenReturnList() throws ErroInesperadoException {
        List<Cliente> clientes = clienteService.listar();

        assertEquals(2, clientes.size());
    }

    @Test(expected = RegistroJaExistenteException.class)
    public void whenSaveExistantEmail_thenThrowExceptionConflict() throws ParametrosInsuficientesException,
            RegistroNaoEncontradoException, ErroInesperadoException, RegistroJaExistenteException, ParametroInvalidoException {
        Cliente cliente = new Cliente();
        cliente.setEmail("duplicado@teste.com");
        cliente.setSenha("1234");

        clienteService.salvar(cliente);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenSaveNull_thenThrowExceptionInvalid() throws ParametrosInsuficientesException,
            RegistroNaoEncontradoException, ErroInesperadoException, RegistroJaExistenteException, ParametroInvalidoException {

        clienteService.salvar(null);
    }

    @Test(expected = ParametrosInsuficientesException.class)
    public void whenSaveNullPassword_thenThrowExceptionInsuficient() throws ParametrosInsuficientesException,
            RegistroNaoEncontradoException, ErroInesperadoException, RegistroJaExistenteException, ParametroInvalidoException {
        Cliente cliente = new Cliente();
        cliente.setEmail("teste@teste.com");
        clienteService.salvar(cliente);
    }

    @Test
    public void whenSaveOk_thenReturnCliente() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametrosInsuficientesException, RegistroJaExistenteException, ParametroInvalidoException {
        Cliente cliente = new Cliente();
        cliente.setEmail("teste@teste.com");
        cliente.setSenha("1234");
        cliente.setNome("teste");

        clienteService.salvar(cliente);

        assertNotNull(cliente.getIdCliente());
    }

    @Test
    public void whenUpdateOk_thenReturnCliente() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametrosInsuficientesException, RegistroJaExistenteException, ParametroInvalidoException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setEmail("teste@teste.com");
        cliente.setSenha("12342");
        cliente.setNome("teste");

        clienteService.salvar(cliente);

        assertNotNull(cliente.getIdCliente());
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenRemoveNull_thenThrowExceptionInvalid() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        clienteService.remover(null);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenRemoveNonExistant_thenThrowExceptionNotFound() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        clienteService.remover(-1L);
    }

    @Test
    public void whenRemoveOk_thenDoNothing() throws ErroInesperadoException, ParametroInvalidoException,
            RegistroNaoEncontradoException {
        clienteService.remover(1L);
    }

}
