package br.com.pcabrantes.prodmanager.service.impl;

import br.com.pcabrantes.prodmanager.entity.Categoria;
import br.com.pcabrantes.prodmanager.exception.*;
import br.com.pcabrantes.prodmanager.repository.CategoriaRepository;
import br.com.pcabrantes.prodmanager.service.CategoriaService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private static Logger LOGGER = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listar() throws ErroInesperadoException {

        List<Categoria> categorias = null;

        try {
            categorias = StreamSupport.stream(categoriaRepository.findAll()
                    .spliterator(), false)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return categorias;
    }

    @Override
    public Categoria obter(Long id) throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException {

        Categoria categoria = null;

        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id precisa ser informado");
            }

            categoria = categoriaRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id));
        } catch (RegistroNaoEncontradoException | ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return categoria;
    }

    @Override
    public Categoria salvar(Categoria categoria) throws ErroInesperadoException, RegistroNaoEncontradoException,
            RegistroJaExistenteException, ParametroInvalidoException {

        Categoria categoriaDB = null;

        try {
            if (categoria == null) {
                throw new ParametroInvalidoException("Categoria não informada");
            }

            if (categoria.getIdCategoria() != null) {
                categoriaDB = this.obter(categoria.getIdCategoria());
                categoriaDB.setCategoria(categoria.getCategoria());
            } else {
                categoriaDB = categoria;
            }

            categoriaRepository.save(categoriaDB);

        } catch (RegistroNaoEncontradoException | ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (ConstraintViolationException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RegistroJaExistenteException();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return categoriaDB;
    }

    @Override
    public void remover(Long id) throws ErroInesperadoException, RegistroNaoEncontradoException,
            ExclusaoNaoPermitidaException, ParametroInvalidoException {
        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id precisa ser informado");
            }

            categoriaRepository.deleteById(id);

        } catch (ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ExclusaoNaoPermitidaException();
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RegistroNaoEncontradoException(id);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }
    }
}
