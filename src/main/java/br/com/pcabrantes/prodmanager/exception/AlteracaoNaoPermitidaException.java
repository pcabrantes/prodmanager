package br.com.pcabrantes.prodmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AlteracaoNaoPermitidaException extends Exception {

    public AlteracaoNaoPermitidaException(String cause) {
        super("Este registro não pode ser alterado. " + cause);
    }

}
