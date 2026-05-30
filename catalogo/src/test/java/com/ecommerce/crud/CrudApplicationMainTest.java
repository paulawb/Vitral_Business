package com.ecommerce.crud;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.SpringApplication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;

class CrudApplicationMainTest {

    @Test
    void constructor_debeCrearInstancia() {
        assertNotNull(new CrudApplication());
    }

    @Test
    void main_debeConfigurarPropiedadesYEjecutarAplicacion() {
        String[] args = new String[]{"--spring.main.web-application-type=none"};

        try (MockedConstruction<SpringApplication> mocked = mockConstruction(SpringApplication.class)) {
            CrudApplication.main(args);

            SpringApplication application = mocked.constructed().get(0);
            ArgumentCaptor<Map<String, Object>> propertiesCaptor = ArgumentCaptor.forClass(Map.class);

            verify(application).setDefaultProperties(propertiesCaptor.capture());
            verify(application).run(args);

            Map<String, Object> properties = propertiesCaptor.getValue();
            assertEquals("jdbc:postgresql://localhost:5434/ecommerce", properties.get("spring.datasource.url"));
            assertEquals("postgres", properties.get("spring.datasource.username"));
            assertEquals("123456789", properties.get("spring.datasource.password"));
            assertEquals("org.postgresql.Driver", properties.get("spring.datasource.driver-class-name"));
            assertEquals("update", properties.get("spring.jpa.hibernate.ddl-auto"));
            assertEquals("true", properties.get("spring.jpa.show-sql"));
            assertEquals("org.hibernate.dialect.PostgreSQLDialect", properties.get("spring.jpa.database-platform"));
            assertEquals("8081", properties.get("server.port"));
        }
    }
}
