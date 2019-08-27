package br.com.pcabrantes.prodmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ParametroInvalidoException extends Exception {

    public ParametroInvalidoException(String descricao) {
        super("Parametro invalido: " + descricao);
    }
}
