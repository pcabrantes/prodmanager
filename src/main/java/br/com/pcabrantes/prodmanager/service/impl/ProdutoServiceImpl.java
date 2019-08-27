package br.com.pcabrantes.prodmanager.service.impl;

import br.com.pcabrantes.prodmanager.entity.Produto;
import br.com.pcabrantes.prodmanager.exception.*;
import br.com.pcabrantes.prodmanager.repository.ProdutoRepository;
import br.com.pcabrantes.prodmanager.service.CategoriaService;
import br.com.pcabrantes.prodmanager.service.ProdutoService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProdutoServiceImpl.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Override
    public Produto obter(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ParametroInvalidoException {

        Produto produto = null;

        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id deve ser informado");
            }

            produto = produtoRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id));
        } catch (RegistroNaoEncontradoException | ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return produto;
    }

    @Override
    public List<Produto> listar() throws ErroInesperadoException {

        List<Produto> produtos = null;

        try {
            produtos = StreamSupport.stream(produtoRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return produtos;
    }

    @Override
    public Produto salvar(Produto produto) throws RegistroNaoEncontradoException, ErroInesperadoException,
            ParametrosInsuficientesException, ParametroInvalidoException {

        Produto produtoDB = null;

        try {
            if (produto == null) {
                throw new ParametroInvalidoException("O produto deve ser informado");
            }

            if (produto.getCategoria() == null || produto.getCategoria().getIdCategoria() == null) {
                throw new ParametrosInsuficientesException("Categoria");
            }
            if (produto.getIdProduto() != null) {
                produtoDB = this.obter(produto.getIdProduto());

                if (!produtoDB.getCategoria().getIdCategoria().equals(produto.getCategoria().getIdCategoria())) {
                    produtoDB.setCategoria(categoriaService.obter(produto.getCategoria().getIdCategoria()));
                }

                produtoDB.setDescricao(produto.getDescricao());
                produtoDB.setFoto(produto.getFoto());
                produtoDB.setPreco(produto.getPreco());
                produtoDB.setProduto(produto.getProduto());
                produtoDB.setQuantidade(produto.getQuantidade());
            } else {
                produtoDB = produto;
                produtoDB.setCategoria(categoriaService.obter(produto.getCategoria().getIdCategoria()));
            }

            if (produtoDB.getQuantidade() < 0) {
                throw new ParametroInvalidoException("Não é possível incuir um produto com quantidade < 0");
            } else if (produtoDB.getPreco().compareTo(BigDecimal.ZERO) < 0) {
                throw new ParametroInvalidoException("Não é possível incuir um produto com preco < 0");
            }

            produtoRepository.save(produtoDB);

        } catch (ConstraintViolationException | DataIntegrityViolationException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RegistroNaoEncontradoException(produto.getCategoria() != null ? produto.getCategoria().getIdCategoria() : null);
        } catch (RegistroNaoEncontradoException | ParametrosInsuficientesException | ParametroInvalidoException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ErroInesperadoException(ex.getMessage());
        }

        return produtoDB;
    }

    @Override
    public void remover(Long id) throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException,
            ParametroInvalidoException {
        try {
            if (id == null) {
                throw new ParametroInvalidoException("O id deve ser informado");
            }

            produtoRepository.deleteById(id);
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
