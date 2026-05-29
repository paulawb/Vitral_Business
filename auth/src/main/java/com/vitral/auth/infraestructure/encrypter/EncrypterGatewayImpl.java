package com.vitral.auth.infraestructure.encrypter;

import com.vitral.auth.domain.model.gateway.EncrypterGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncrypterGatewayImpl implements EncrypterGateway {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public String encrypt(String password) {return encoder.encode(password);}

    @Override
    public boolean matches(String rawPassword,String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}