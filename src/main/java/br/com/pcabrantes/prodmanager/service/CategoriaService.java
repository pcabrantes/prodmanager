package br.com.pcabrantes.prodmanager.service;

import br.com.pcabrantes.prodmanager.entity.Categoria;
import br.com.pcabrantes.prodmanager.exception.*;

import java.util.List;

public interface CategoriaService {

    Categoria obter(Long id) throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException;
    List<Categoria> listar() throws ErroInesperadoException;
    Categoria salvar(Categoria categoria) throws ErroInesperadoException, RegistroNaoEncontradoException, RegistroJaExistenteException, ParametroInvalidoException;
    void remover(Long id) throws ErroInesperadoException, RegistroNaoEncontradoException, ExclusaoNaoPermitidaException, ParametroInvalidoException;

}
