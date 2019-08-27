package br.com.pcabrantes.prodmanager.controller;

import br.com.pcabrantes.prodmanager.dto.LoginRequestDTO;
import br.com.pcabrantes.prodmanager.dto.LoginResponseDTO;
import br.com.pcabrantes.prodmanager.exception.LoginOuSenhaInvalidosException;
import br.com.pcabrantes.prodmanager.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Efetua o login na aplicacao, obtendo um token jwt
     *
     * @param loginRequestDTO
     * @return
     * @throws LoginOuSenhaInvalidosException
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) throws LoginOuSenhaInvalidosException {
        return authenticationService.login(loginRequestDTO);
    }

}
