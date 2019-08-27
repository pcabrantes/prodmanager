package br.com.pcabrantes.prodmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ExclusaoNaoPermitidaException extends Exception {

    public ExclusaoNaoPermitidaException() {
        super("Não é possível excluir este registro, pois ele está ligado a outros registros.");
    }
}
