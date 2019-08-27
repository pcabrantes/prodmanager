package br.com.pcabrantes.prodmanager.service.test;

import br.com.pcabrantes.prodmanager.entity.Categoria;
import br.com.pcabrantes.prodmanager.entity.Produto;
import br.com.pcabrantes.prodmanager.exception.*;
import br.com.pcabrantes.prodmanager.repository.ProdutoRepository;
import br.com.pcabrantes.prodmanager.service.CategoriaService;
import br.com.pcabrantes.prodmanager.service.ProdutoService;
import br.com.pcabrantes.prodmanager.service.impl.ProdutoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProdutoServiceTest {

    @TestConfiguration
    static class ProdutoServiceImplTestContextConfiguration {

        @Bean
        public ProdutoService produtoService() {
            return new ProdutoServiceImpl();
        }
    }

    @Autowired
    private ProdutoService produtoService;

    @MockBean
    private ProdutoRepository produtoRepository;

    @MockBean
    private CategoriaService categoriaService;

    @Before
    public void setUp() throws RegistroNaoEncontradoException, ParametroInvalidoException, ErroInesperadoException {

        when(produtoRepository.findById(longThat(argument -> argument > 0L))).thenAnswer(invocation -> {
            Produto produto = new Produto();
            produto.setIdProduto((Long) invocation.getArguments()[0]);
            return Optional.of(produto);
        });

        when(produtoRepository.findById(longThat(argument -> argument < 0L))).thenAnswer(invocation -> {
            return Optional.empty();
        });

        when(produtoRepository.findAll()).thenReturn(Arrays.asList(new Produto(), new Produto()));

        when(produtoRepository.save(argThat(argument -> argument != null && argument.getIdProduto() == null))).thenAnswer(invocation -> {
            Produto produto = (Produto) invocation.getArguments()[0];
            produto.setIdProduto(new Random().nextLong());
            return produto;
        });

        when(produtoRepository.save(argThat(argument -> argument != null && argument.getIdProduto() != null))).thenAnswer(invocation -> {
            Produto produto = (Produto) invocation.getArguments()[0];
            return produto;
        });

        Categoria categoria1 = new Categoria();
        categoria1.setIdCategoria(1L);

        Categoria categoria2 = new Categoria();
        categoria2.setIdCategoria(2L);

        Produto produto2 = new Produto();
        produto2.setIdProduto(2L);
        produto2.setCategoria(categoria2);

        when(categoriaService.obter(1L)).thenReturn(categoria1);
        when(categoriaService.obter(2L)).thenReturn(categoria2);
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));
        when(produtoRepository.findById(-1L)).thenReturn(Optional.empty());

        doThrow(DataIntegrityViolationException.class).when(produtoRepository).deleteById(2L);
        doThrow(EmptyResultDataAccessException.class).when(produtoRepository).deleteById(-1L);

    }

    @Test
    public void whenGetExisting_thenReturnProdutoWithSameId() throws ErroInesperadoException, ParametroInvalidoException, RegistroNaoEncontradoException {
        Produto produto = produtoService.obter(1L);

        assertEquals(1L, produto.getIdProduto().longValue());
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenGetNonExisting_thenThrowExceptionNotFound() throws ErroInesperadoException, ParametroInvalidoException, RegistroNaoEncontradoException {
        Produto produto = produtoService.obter(-1L);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenGeNull_thenThrowExceptionInvalid() throws ErroInesperadoException, ParametroInvalidoException, RegistroNaoEncontradoException {
        Produto produto = produtoService.obter(null);
    }

    @Test
    public void whenListAll_thenReturnList() throws ErroInesperadoException {
        List<Produto> produtos = produtoService.listar();

        assertEquals(2, produtos.size());
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenSaveNull_thenThrowExceptionInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametrosInsuficientesException, ParametroInvalidoException {
        produtoService.salvar(null);
    }

    @Test
    public void whenSaveNewProduct_thenReturnProductSaved() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametrosInsuficientesException, ParametroInvalidoException {
        Produto produto = new Produto();
        produto.setCategoria(categoriaService.obter(1L));
        produto.setPreco(BigDecimal.TEN);
        produtoService.salvar(produto);

        assertNotNull(produto.getIdProduto());
    }

    @Test(expected = ParametrosInsuficientesException.class)
    public void whenSaveProdutoWithoutCategoria_thenThrowExceptionInsuficient() throws ErroInesperadoException,
            RegistroNaoEncontradoException, ParametrosInsuficientesException, ParametroInvalidoException {
        Produto produto = new Produto();

        produtoService.salvar(produto);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenSaveProdutoQtdLessThanZero_thenThrowExceptionInvalid() throws ErroInesperadoException,
            RegistroNaoEncontradoException, ParametrosInsuficientesException, ParametroInvalidoException {
        Produto produto = new Produto();
        produto.setQuantidade(-1);
        produto.setCategoria(categoriaService.obter(1L));

        produtoService.salvar(produto);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenSaveProdutoPrecoLessThanZero_thenThrowExceptionInvalid() throws ErroInesperadoException,
            RegistroNaoEncontradoException, ParametrosInsuficientesException, ParametroInvalidoException {
        Produto produto = new Produto();
        produto.setQuantidade(1);
        produto.setPreco(BigDecimal.valueOf(-1));
        produto.setCategoria(categoriaService.obter(1L));

        produtoService.salvar(produto);
    }

    @Test
    public void whenUpdateProdutoCategoria_thenReturnProdutoWithNewCategoria() throws RegistroNaoEncontradoException,
            ParametroInvalidoException, ErroInesperadoException, ParametrosInsuficientesException {
        Produto produto = new Produto();
        produto.setIdProduto(2L);
        produto.setQuantidade(2);
        produto.setPreco(BigDecimal.TEN);
        produto.setCategoria(categoriaService.obter(1L));

        produtoService.salvar(produto);

        assertEquals(1L, produto.getCategoria().getIdCategoria().longValue());
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenUpdateProdutoNonexistant_thenReturnProdutoWithNewCategoria() throws RegistroNaoEncontradoException,
            ParametroInvalidoException, ErroInesperadoException, ParametrosInsuficientesException {
        Produto produto = new Produto();
        produto.setIdProduto(-1L);
        produto.setQuantidade(2);
        produto.setPreco(BigDecimal.TEN);
        produto.setCategoria(categoriaService.obter(1L));

        produtoService.salvar(produto);

        assertEquals(1L, produto.getCategoria().getIdCategoria().longValue());
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenRemoveNull_thenThrowExceptionInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametroInvalidoException, ExclusaoNaoPermitidaException {
        produtoService.remover(null);
    }

    @Test(expected = ExclusaoNaoPermitidaException.class)
    public void whenRemoveDependent_thenThrowExceptionNotPermited() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametroInvalidoException, ExclusaoNaoPermitidaException {
        produtoService.remover(2L);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenRemoveNonExistant_thenThrowExceptionNotFound() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametroInvalidoException, ExclusaoNaoPermitidaException {
        produtoService.remover(-1L);
    }

    @Test
    public void whenRemoveOk_thenDoNothing() throws ErroInesperadoException, RegistroNaoEncontradoException,
            ParametroInvalidoException, ExclusaoNaoPermitidaException {
        produtoService.remover(1L);
    }
}
