package br.com.pcabrantes.prodmanager.adapter;

import br.com.pcabrantes.prodmanager.dto.ClienteDTO;
import br.com.pcabrantes.prodmanager.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteAdapter {

    public ClienteDTO toDTO(Cliente cliente) {

        ClienteDTO dto = null;

        if (cliente != null) {
            dto = new ClienteDTO();
            dto.setId(cliente.getIdCliente());
            dto.setBairro(cliente.getBairro());
            dto.setCep(cliente.getCep());
            dto.setCidade(cliente.getCidade());
            dto.setEmail(cliente.getEmail());
            dto.setEstado(cliente.getEstado());
            dto.setNome(cliente.getNome());
            dto.setRua(cliente.getRua());
            dto.setSenha("*****");
        }

        return dto;
    }

    public Cliente fromDTO(ClienteDTO dto) {

        Cliente cliente = null;

        if (dto != null) {
            cliente = new Cliente();
            cliente.setIdCliente(dto.getId());
            cliente.setSenha(dto.getSenha());
            cliente.setRua(dto.getRua());
            cliente.setNome(dto.getNome());
            cliente.setEstado(dto.getEstado());
            cliente.setEmail(dto.getEmail());
            cliente.setCidade(dto.getCidade());
            cliente.setCep(dto.getCep());
            cliente.setBairro(dto.getBairro());
        }

        return cliente;
    }
}
