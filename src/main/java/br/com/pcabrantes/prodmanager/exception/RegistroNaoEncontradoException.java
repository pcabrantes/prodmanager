package br.com.pcabrantes.prodmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RegistroNaoEncontradoException extends Exception {

    public RegistroNaoEncontradoException(Long id) {
        super("Registro [" + id + "] não encontrado");
    }
}
