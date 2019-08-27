package br.com.pcabrantes.prodmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class LoginOuSenhaInvalidosException extends Exception {

    public LoginOuSenhaInvalidosException() {
        super("Login ou senha inválidos");
    }
}
