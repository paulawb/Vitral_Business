package com.vitral.auth.infraestructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        classes = SecurityConfigTest.TestApp.class,
        properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
)
class SecurityConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void filterChain_debeRegistrarseEnElContexto() {
        SecurityFilterChain filterChain = applicationContext.getBean(SecurityFilterChain.class);

        assertNotNull(filterChain);
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @Import(SecurityConfig.class)
    static class TestApp {
    }
}
