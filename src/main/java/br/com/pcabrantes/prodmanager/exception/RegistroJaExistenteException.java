package br.com.pcabrantes.prodmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class RegistroJaExistenteException extends Exception {

    public RegistroJaExistenteException() {
        super("Registro já existente");
    }
}
