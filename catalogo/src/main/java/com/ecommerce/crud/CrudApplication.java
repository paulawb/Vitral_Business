package com.ecommerce.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CrudApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(CrudApplication.class);

		Map<String, Object> propiedadesPorDefecto = new HashMap<>();
		propiedadesPorDefecto.put("spring.datasource.url", "jdbc:postgresql://localhost:5434/ecommerce");
		propiedadesPorDefecto.put("spring.datasource.username", "postgres");
		propiedadesPorDefecto.put("spring.datasource.password", "123456789");
		propiedadesPorDefecto.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
		propiedadesPorDefecto.put("spring.jpa.hibernate.ddl-auto", "update");
		propiedadesPorDefecto.put("spring.jpa.show-sql", "true");
		propiedadesPorDefecto.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
		propiedadesPorDefecto.put("server.port", "8081");

		application.setDefaultProperties(propiedadesPorDefecto);
		application.run(args);
	}

}
