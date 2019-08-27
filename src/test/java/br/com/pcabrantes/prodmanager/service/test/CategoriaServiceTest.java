package br.com.pcabrantes.prodmanager.service.test;

import br.com.pcabrantes.prodmanager.entity.Categoria;
import br.com.pcabrantes.prodmanager.exception.*;
import br.com.pcabrantes.prodmanager.repository.CategoriaRepository;
import br.com.pcabrantes.prodmanager.service.CategoriaService;
import br.com.pcabrantes.prodmanager.service.impl.CategoriaServiceImpl;
import org.hibernate.exception.ConstraintViolationException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CategoriaServiceTest {

    @TestConfiguration
    static class CategoriaServiceImplTestContextConfiguration {

        @Bean
        public CategoriaService categoriaService() {
            return new CategoriaServiceImpl();
        }
    }

    @Autowired
    private CategoriaService categoriaService;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @Before
    public void setUp() {
        Categoria categoria1 = new Categoria();
        categoria1.setIdCategoria(1L);
        categoria1.setCategoria("Categoria 1");

        Categoria categoria2 = new Categoria();
        categoria2.setIdCategoria(2L);
        categoria2.setCategoria("Categoria 2");

        Categoria novaCategoria = new Categoria();
        novaCategoria.setCategoria("Nova Categoria");

        Categoria categoriaDupl = new Categoria();
        categoriaDupl.setCategoria("Categoria 2");

        when(categoriaRepository.findAll())
                .thenReturn(Arrays.asList(categoria1, categoria2));

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria1));

        when(categoriaRepository.save(novaCategoria))
                .thenAnswer(invocation -> {
                    Categoria c = (Categoria) invocation.getArguments()[0];
                    c.setIdCategoria(new Random().nextLong());
                    return c;
                });

        when(categoriaRepository.save(categoriaDupl))
                .thenThrow(ConstraintViolationException.class);

        doThrow(EmptyResultDataAccessException.class)
                .when(categoriaRepository).deleteById(-1L);

        doThrow(DataIntegrityViolationException.class)
                .when(categoriaRepository).deleteById(20L);
    }

    @Test
    public void whenListAll_thenReturnList() throws ErroInesperadoException {
        List<Categoria> categorias = categoriaService.listar();

        assertEquals(2, categorias.size());
    }

    @Test
    public void whenGetId1_thenReturnCategoria1() throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException {
        Categoria categoria = categoriaService.obter(1L);

        assertNotNull(categoria);
        assertEquals(1L, categoria.getIdCategoria().longValue());
        assertEquals("Categoria 1", categoria.getCategoria());
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenGetId5_thenThrowExceptionNotFound() throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException {
        Categoria categoria = categoriaService.obter(5L);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenGetIdNull_thenThrowExceptionInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException {
        Categoria categoria = categoriaService.obter(null);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenSaveNullCategoria_thenThrowExceptonInvalid() throws ErroInesperadoException, RegistroNaoEncontradoException, RegistroJaExistenteException, ParametroInvalidoException {
        categoriaService.salvar(null);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenUpdateInexistentCategoria_thenThrowExceptionNotFound() throws ErroInesperadoException, RegistroNaoEncontradoException, RegistroJaExistenteException, ParametroInvalidoException {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(10L);
        categoria.setCategoria("Test not found");

        categoriaService.salvar(categoria);
    }

    @Test
    public void whenSaveNewCategoria_thenReturnObjectSaved() throws ErroInesperadoException, RegistroNaoEncontradoException,
            RegistroJaExistenteException, ParametroInvalidoException {
        Categoria categoria = new Categoria();
        categoria.setCategoria("Nova Categoria");

        categoriaService.salvar(categoria);

        assertNotNull(categoria.getIdCategoria());
    }

    @Test
    public void whenUpdateCategoria_thenReturnObjectSaved() throws ErroInesperadoException, RegistroNaoEncontradoException,
            RegistroJaExistenteException, ParametroInvalidoException {
        Categoria categoria1 = new Categoria();
        categoria1.setIdCategoria(1L);
        categoria1.setCategoria("Categoria Alterada");

        categoriaService.salvar(categoria1);
    }

    @Test(expected = RegistroJaExistenteException.class)
    public void whenSaveExistingCategoria_thenThrowExceptionExisting() throws ErroInesperadoException, RegistroNaoEncontradoException,
            RegistroJaExistenteException, ParametroInvalidoException {
        Categoria categoria = new Categoria();
        categoria.setCategoria("Categoria 2");

        categoriaService.salvar(categoria);
    }

    @Test(expected = ParametroInvalidoException.class)
    public void whenRemoveNull_thenThrowExceptionInvalid() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        categoriaService.remover(null);
    }

    @Test(expected = ExclusaoNaoPermitidaException.class)
    public void whenRemoveDependent_thenThrowExceptionNotPermited() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        categoriaService.remover(20L);
    }

    @Test(expected = RegistroNaoEncontradoException.class)
    public void whenRemoveNotFound_thenThrowExceptionNotFound() throws RegistroNaoEncontradoException, ErroInesperadoException, ExclusaoNaoPermitidaException, ParametroInvalidoException {
        categoriaService.remover(-1L);
    }

    @Test
    public void whenRemoveOk_thenDoNothing() throws ErroInesperadoException, RegistroNaoEncontradoException, ParametroInvalidoException, ExclusaoNaoPermitidaException {
        categoriaService.remover(2L);
    }
}
