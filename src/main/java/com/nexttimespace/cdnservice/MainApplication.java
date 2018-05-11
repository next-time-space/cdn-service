package com.nexttimespace.cdnservice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication 
public class MainApplication {
	public static void main(String[] args) throws URISyntaxException, ClassNotFoundException, SQLException, IOException {
		SpringApplication.run(MainApplication.class, args);
	}
}
