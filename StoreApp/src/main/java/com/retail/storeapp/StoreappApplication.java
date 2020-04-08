package com.retail.storeapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.IOException;

@SpringBootApplication
public class StoreappApplication {

	/*private static EmbeddedDatabase embeddedDatabase;*/

	public static void main(String[] args) {
		SpringApplication.run(StoreappApplication.class, args);
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		MappingJackson2HttpMessageConverter converter =
				new MappingJackson2HttpMessageConverter(mapper);
		return converter;
	}

	/*@Bean(name="dataSource")
	public static EmbeddedDatabase getEmbeddedDatabase(){
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		if()
		builder.setType(EmbeddedDatabaseType.H2)

	}*/

	/*public static void main(String[] args) throws IOException {
		Runtime.getRuntime().exec("cmd /c for /F \"TOKENS=5\" %G IN ('netstat -ano ^|findstr \"0.0.0.0:8123\"') DO taskkill /pid %G /F");
		Runtime.getRuntime().exec("cmd /c start javaw -Xmx1024m -jar C:/data/storeapp.jar");
	}*/
}
