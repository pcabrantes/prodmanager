package br.com.pcabrantes.prodmanager.adapter;

import br.com.pcabrantes.prodmanager.dto.CategoriaDTO;
import br.com.pcabrantes.prodmanager.entity.Categoria;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CategoriaAdapter {

    public CategoriaDTO toDTO(Categoria categoria) {
        CategoriaDTO dto = null;

        if (categoria != null) {
            dto = new CategoriaDTO(categoria.getIdCategoria(), categoria.getCategoria());
        }

        return dto;
    }

    public Categoria fromDTO(CategoriaDTO dto) {
        Categoria categoria = null;

        if (dto != null) {
            categoria = new Categoria();
            categoria.setIdCategoria(dto.getId());

            if (!StringUtils.isEmpty(dto.getCategoria())) {
                categoria.setCategoria(dto.getCategoria().trim());
            }
        }

        return categoria;
    }
}
