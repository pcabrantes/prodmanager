package br.com.pcabrantes.prodmanager.service.impl;

import br.com.pcabrantes.prodmanager.config.security.SecurityService;
import br.com.pcabrantes.prodmanager.dto.LoginRequestDTO;
import br.com.pcabrantes.prodmanager.dto.LoginResponseDTO;
import br.com.pcabrantes.prodmanager.exception.LoginOuSenhaInvalidosException;
import br.com.pcabrantes.prodmanager.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private SecurityService securityService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) throws LoginOuSenhaInvalidosException {

        String token = securityService.login(request.getUsername(), request.getPassword());

        if (StringUtils.isEmpty(token)) {
            throw new LoginOuSenhaInvalidosException();
        }

        return new LoginResponseDTO(token);
    }
}
