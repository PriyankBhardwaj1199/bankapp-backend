package com.app.bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The SpringBoot Banking System",
				description = "A Spring boot powered backend banking application.",
				version = "v1.0",
				contact = @Contact(
						name = "Priyank Bhardwaj",
						email = "priyankbhardwaj@gmail.com"
					//	url = "http://priyankbhardwaj.com"
				),
				license = @License(
						name = "The SillyGuy Codes"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The SillyGuy codes banking application",
				url = "http://priyankbhardwaj.com"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
