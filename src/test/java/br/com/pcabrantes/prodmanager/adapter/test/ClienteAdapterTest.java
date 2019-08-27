package br.com.pcabrantes.prodmanager.adapter.test;

import br.com.pcabrantes.prodmanager.adapter.ClienteAdapter;
import br.com.pcabrantes.prodmanager.dto.ClienteDTO;
import br.com.pcabrantes.prodmanager.entity.Cliente;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class ClienteAdapterTest {

    @TestConfiguration
    static class ClienteAdapterTestContextConfiguration {
        @Bean
        public ClienteAdapter clienteAdapter() {
            return new ClienteAdapter();
        }
    }

    @Autowired
    private ClienteAdapter clienteAdapter;

    @Test
    public void whenClienteIsNull_ThenReturnNull() {
        ClienteDTO clienteDTO = clienteAdapter.toDTO(null);

        assertNull(clienteDTO);
    }

    @Test
    public void whenClienteDTOIsNull_ThenReturnNull() {
        Cliente cliente = clienteAdapter.fromDTO(null);

        assertNull(cliente);
    }

    @Test
    public void whenClienteIsNotNull_ThenReturnClienteDTO() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setSenha("12345");
        cliente.setBairro("bairro");
        cliente.setCep("02938-023");
        cliente.setCidade("cidade");
        cliente.setEmail("email");
        cliente.setEstado("estado");
        cliente.setNome("Nome");
        cliente.setRua("Rua");

        ClienteDTO dto = clienteAdapter.toDTO(cliente);

        assertEquals(cliente.getIdCliente(), dto.getId());
        assertEquals(cliente.getEmail(), dto.getEmail());
        assertEquals("*****", dto.getSenha());
        assertEquals(cliente.getBairro(), dto.getBairro());
        assertEquals(cliente.getCep(), dto.getCep());
        assertEquals(cliente.getCidade(), dto.getCidade());
        assertEquals(cliente.getEstado(), dto.getEstado());
        assertEquals(cliente.getNome(), dto.getNome());
        assertEquals(cliente.getRua(), dto.getRua());
    }

    @Test
    public void whenClienteDTOIsNotNull_ThenReturnCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(1L);
        dto.setSenha("12345");
        dto.setBairro("bairro");
        dto.setCep("02938-023");
        dto.setCidade("cidade");
        dto.setEmail("email");
        dto.setEstado("estado");
        dto.setNome("Nome");
        dto.setRua("Rua");

        Cliente cliente = clienteAdapter.fromDTO(dto);

        assertEquals(dto.getId(), cliente.getIdCliente());
        assertEquals(dto.getEmail(), cliente.getEmail());
        assertEquals(dto.getSenha(), cliente.getSenha());
        assertEquals(dto.getBairro(), cliente.getBairro());
        assertEquals(dto.getCep(), cliente.getCep());
        assertEquals(dto.getCidade(), cliente.getCidade());
        assertEquals(dto.getEstado(), cliente.getEstado());
        assertEquals(dto.getNome(), cliente.getNome());
        assertEquals(dto.getRua(), cliente.getRua());
    }
}
