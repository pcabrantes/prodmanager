package br.com.pcabrantes.prodmanager.service.impl;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import br.com.pcabrantes.prodmanager.exception.*;
import br.com.pcabrantes.prodmanager.repository.ClienteRepository;
import br.com.pcabrantes.prodmanager.service.ClienteService;
import br.com.pcabrantes.prodmanager.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static Logger LOGGER = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Cliente obter(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException {
        Cliente cliente = null;

        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id deve ser informado");
            }

            cliente = clienteRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id));
        } catch (RegistroNaoEncontradoException | ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return cliente;
    }

    @Override
    public List<Cliente> listar() throws ErroInesperadoException {
        List<Cliente> clientes = null;

        try {
            clientes = StreamSupport.stream(clienteRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return clientes;
    }

    @Override
    public Cliente salvar(Cliente cliente) throws RegistroNaoEncontradoException, ParametrosInsuficientesException, ErroInesperadoException, RegistroJaExistenteException, ParametroInvalidoException {

        Cliente clienteDB = null;

        try {
            if (cliente == null) {
                throw new ParametroInvalidoException("O cliente deve ser informado");
            }

            if (cliente.getIdCliente() != null) {
                clienteDB = this.obter(cliente.getIdCliente());
                clienteDB.setBairro(cliente.getBairro());
                clienteDB.setCep(cliente.getCep());
                clienteDB.setCidade(cliente.getCidade());
                clienteDB.setEmail(cliente.getEmail());
                clienteDB.setEstado(cliente.getEstado());
                clienteDB.setNome(cliente.getNome());
                clienteDB.setRua(cliente.getRua());
                clienteDB.setSenha(cliente.getSenha());
            } else {
                clienteDB = cliente;
            }

            if (StringUtils.isEmpty(cliente.getSenha()) || StringUtils.isEmpty(cliente.getEmail())) {
                throw new ParametrosInsuficientesException("email/senha");
            }

            Cliente clienteEmail = clienteRepository.findByEmail(clienteDB.getEmail()).orElse(null);

            if (clienteEmail != null && (clienteDB.getIdCliente() == null || !clienteEmail.getIdCliente().equals(clienteDB.getIdCliente()))) {
                throw new RegistroJaExistenteException();
            }

            clienteDB.setSenha(passwordEncoder.encode(cliente.getSenha()));

            clienteRepository.save(clienteDB);

        } catch (RegistroNaoEncontradoException | ParametrosInsuficientesException | RegistroJaExistenteException | ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return clienteDB;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remover(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException {
        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id deve ser informado");
            }

            pedidoService.removerPorCliente(id);
            clienteRepository.deleteById(id);
        } catch (ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RegistroNaoEncontradoException(id);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }
    }
}
