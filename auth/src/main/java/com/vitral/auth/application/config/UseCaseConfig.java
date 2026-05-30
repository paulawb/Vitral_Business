package com.vitral.auth.application.config;

import com.vitral.auth.domain.model.gateway.EmailGateway;
import com.vitral.auth.domain.model.gateway.EncrypterGateway;
import com.vitral.auth.domain.model.gateway.UsuarioGateway;
import com.vitral.auth.domain.usecase.UsuarioUseCase;
import com.vitral.auth.infraestructure.email.EmailGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class UseCaseConfig {
    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateWay, EncrypterGateway encrypterGateway) {
        return new UsuarioUseCase(usuarioGateWay, encrypterGateway);
    }

    @Bean
    public EmailGateway emailGateway(JavaMailSender mailSender) {
        return new EmailGatewayImpl(mailSender);
    }
}
