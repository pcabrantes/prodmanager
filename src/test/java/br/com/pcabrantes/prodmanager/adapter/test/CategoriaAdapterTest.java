package br.com.pcabrantes.prodmanager.adapter.test;

import br.com.pcabrantes.prodmanager.adapter.CategoriaAdapter;
import br.com.pcabrantes.prodmanager.dto.CategoriaDTO;
import br.com.pcabrantes.prodmanager.entity.Categoria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class CategoriaAdapterTest {

    @TestConfiguration
    static class CategoriaAdapterTestContextConfiguration {
        @Bean
        public CategoriaAdapter categoriaAdapter() {
            return new CategoriaAdapter();
        }
    }

    @Autowired
    private CategoriaAdapter categoriaAdapter;

    @Test
    public void whenCategoriaIsNull_thenReturnNull() {
        CategoriaDTO dto = categoriaAdapter.toDTO(null);

        assertNull(dto);
    }

    @Test
    public void whenCategoriaDTOIsNull_thenReturnNull() {
        Categoria categoria = categoriaAdapter.fromDTO(null);

        assertNull(categoria);
    }

    @Test
    public void whenCategoriaDTOIsNotNull_thenReturnCategoria() {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setCategoria("Categoria");

        Categoria categoria = categoriaAdapter.fromDTO(categoriaDTO);

        assertEquals(categoriaDTO.getId(), categoria.getIdCategoria());
        assertEquals(categoriaDTO.getCategoria(), categoria.getCategoria());
    }

    @Test
    public void whenCategoriaIsNotNull_thenReturnCategoriaDTO() {
        Categoria categoria = new Categoria(1L, "Categoria");

        CategoriaDTO dto = categoriaAdapter.toDTO(categoria);

        assertEquals(categoria.getIdCategoria(), dto.getId());
        assertEquals(categoria.getCategoria(), dto.getCategoria());
    }

}
