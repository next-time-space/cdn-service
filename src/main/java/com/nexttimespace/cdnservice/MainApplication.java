package com.nexttimespace.cdnservice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableCaching
public class MainApplication {
	public static void main(String[] args) throws URISyntaxException, ClassNotFoundException, SQLException, IOException {
		SpringApplication.run(MainApplication.class, args);
	}
}
