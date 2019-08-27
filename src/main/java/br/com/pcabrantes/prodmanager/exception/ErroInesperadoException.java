package br.com.pcabrantes.prodmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ErroInesperadoException extends Exception {

    public ErroInesperadoException(String message) {
        super(message);
    }
}
