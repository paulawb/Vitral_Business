package com.vitral.auth.application.config;

import com.vitral.auth.domain.model.gateway.EmailGateway;
import com.vitral.auth.domain.model.gateway.EncrypterGateway;
import com.vitral.auth.domain.model.gateway.UsuarioGateway;
import com.vitral.auth.domain.usecase.UsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCaseConfigTest {

    @Test
    void usuarioUseCase_debeCrearBean() {
        UseCaseConfig config = new UseCaseConfig();
        UsuarioGateway usuarioGateway = mock(UsuarioGateway.class);
        EncrypterGateway encrypterGateway = mock(EncrypterGateway.class);

        UsuarioUseCase useCase = config.usuarioUseCase(usuarioGateway, encrypterGateway);

        assertNotNull(useCase);
    }

    @Test
    void emailGateway_debeCrearBean() {
        UseCaseConfig config = new UseCaseConfig();
        JavaMailSender mailSender = mock(JavaMailSender.class);

        EmailGateway emailGateway = config.emailGateway(mailSender);

        assertNotNull(emailGateway);
    }
}
