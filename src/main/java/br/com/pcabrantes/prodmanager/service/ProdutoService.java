package br.com.pcabrantes.prodmanager.service;

import br.com.pcabrantes.prodmanager.entity.Produto;
import br.com.pcabrantes.prodmanager.exception.*;

import java.util.List;

public interface ProdutoService {

    Produto obter(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException;
    List<Produto> listar() throws ErroInesperadoException;
    Produto salvar(Produto produto) throws RegistroNaoEncontradoException, ParametrosInsuficientesException, ErroInesperadoException, ParametroInvalidoException;
    void remover(Long id) throws ErroInesperadoException, RegistroNaoEncontradoException, ExclusaoNaoPermitidaException, ParametroInvalidoException;
}
