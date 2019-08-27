package br.com.pcabrantes.prodmanager.adapter;

import br.com.pcabrantes.prodmanager.dto.CategoriaDTO;
import br.com.pcabrantes.prodmanager.dto.ProdutoDTO;
import br.com.pcabrantes.prodmanager.entity.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdutoAdapter {

    @Autowired
    private CategoriaAdapter categoriaAdapter;

    public ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO produtoDTO = null;

        if (produto != null) {
            produtoDTO = new ProdutoDTO();
            produtoDTO.setId(produto.getIdProduto());
            produtoDTO.setDescricao(produto.getDescricao());
            produtoDTO.setFoto(produto.getFoto());
            produtoDTO.setPreco(produto.getPreco());
            produtoDTO.setProduto(produto.getProduto());
            produtoDTO.setQuantidade(produto.getQuantidade());

            CategoriaDTO categoriaDTO = categoriaAdapter.toDTO(produto.getCategoria());

            produtoDTO.setCategoria(categoriaDTO);
        }

        return produtoDTO;
    }

    public Produto fromDTO(ProdutoDTO dto) {

        Produto produto = null;

        if (dto != null) {
            produto = new Produto();
            produto.setIdProduto(dto.getId());
            produto.setQuantidade(dto.getQuantidade());

            if (dto.getPreco() == null) {
                produto.setPreco(BigDecimal.ZERO);
            } else {
                produto.setPreco(dto.getPreco());
            }

            produto.setProduto(dto.getProduto());
            produto.setDescricao(dto.getDescricao());
            produto.setFoto(dto.getFoto());
            produto.setCategoria(categoriaAdapter.fromDTO(dto.getCategoria()));

        }

        return produto;
    }
}
