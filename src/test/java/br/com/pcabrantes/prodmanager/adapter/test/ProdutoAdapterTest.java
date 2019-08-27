package br.com.pcabrantes.prodmanager.adapter.test;

import br.com.pcabrantes.prodmanager.adapter.CategoriaAdapter;
import br.com.pcabrantes.prodmanager.adapter.ProdutoAdapter;
import br.com.pcabrantes.prodmanager.dto.CategoriaDTO;
import br.com.pcabrantes.prodmanager.dto.ProdutoDTO;
import br.com.pcabrantes.prodmanager.entity.Categoria;
import br.com.pcabrantes.prodmanager.entity.Produto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class ProdutoAdapterTest {

    @TestConfiguration
    static class ProdutoAdapterTestContextConfiguration {
        @Bean
        public ProdutoAdapter produtoAdapter() {
            return new ProdutoAdapter();
        }

        @Bean
        public CategoriaAdapter categoriaAdapter() {
            return new CategoriaAdapter();
        }
    }

    @Autowired
    private ProdutoAdapter produtoAdapter;

    @Test
    public void whenProdutoIsNull_ThenReturnNull() {
        ProdutoDTO dto = produtoAdapter.toDTO(null);

        assertNull(dto);
    }

    @Test
    public void whenProdutoDTOIsNull_ThenReturnNull() {
        Produto produto = produtoAdapter.fromDTO(null);

        assertNull(produto);
    }

    @Test
    public void whenProdutoIsNotNull_ThenReturnProdutoDTO() {
        Produto produto = new Produto();
        produto.setIdProduto(1L);
        produto.setQuantidade(1);
        produto.setFoto("foto.png");
        produto.setDescricao("descricao");
        produto.setProduto("produto");
        produto.setPreco(BigDecimal.TEN);
        produto.setCategoria(new Categoria());

        ProdutoDTO dto = produtoAdapter.toDTO(produto);

        assertEquals(produto.getIdProduto(), dto.getId());
        assertEquals(produto.getPreco(), dto.getPreco());
        assertEquals(produto.getProduto(), dto.getProduto());
        assertEquals(produto.getQuantidade(), dto.getQuantidade());
        assertEquals(produto.getDescricao(), dto.getDescricao());
        assertEquals(produto.getFoto(), dto.getFoto());
    }

    @Test
    public void whenProdutoDTOIsNotNull_ThenReturnProduto() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(1L);
        dto.setQuantidade(1);
        dto.setFoto("foto.png");
        dto.setDescricao("descricao");
        dto.setProduto("produto");
        dto.setPreco(BigDecimal.TEN);
        dto.setCategoria(new CategoriaDTO());

        Produto produto = produtoAdapter.fromDTO(dto);

        assertEquals(dto.getId(), produto.getIdProduto());
        assertEquals(dto.getPreco(), produto.getPreco());
        assertEquals(dto.getProduto(), produto.getProduto());
        assertEquals(dto.getQuantidade(), produto.getQuantidade());
        assertEquals(dto.getDescricao(), produto.getDescricao());
        assertEquals(dto.getFoto(), produto.getFoto());
    }


    @Test
    public void whenProdutoDTOPrecoIsNull_ThenReturnProdutoPrecoZero() {
        ProdutoDTO dto = new ProdutoDTO();
        Produto produto = produtoAdapter.fromDTO(dto);

        assertEquals(BigDecimal.ZERO, produto.getPreco());
    }
}
