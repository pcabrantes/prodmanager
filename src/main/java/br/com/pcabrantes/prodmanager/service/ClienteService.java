package br.com.pcabrantes.prodmanager.service;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import br.com.pcabrantes.prodmanager.exception.*;

import java.util.List;

public interface ClienteService {

    Cliente obter(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException;
    List<Cliente> listar() throws ErroInesperadoException;
    Cliente salvar(Cliente cliente) throws RegistroNaoEncontradoException, ParametrosInsuficientesException, ErroInesperadoException, RegistroJaExistenteException, ParametroInvalidoException;
    void remover(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException;
}
