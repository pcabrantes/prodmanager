package br.com.pcabrantes.prodmanager.service;

import br.com.pcabrantes.prodmanager.dto.LoginRequestDTO;
import br.com.pcabrantes.prodmanager.dto.LoginResponseDTO;
import br.com.pcabrantes.prodmanager.exception.LoginOuSenhaInvalidosException;

public interface AuthenticationService {

    LoginResponseDTO login(LoginRequestDTO request) throws LoginOuSenhaInvalidosException;
}
