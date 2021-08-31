package com.lamvinhtuong.thuctaptotnghiep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"controllers", "service"})
@EntityScan(basePackages = {"models"})
@EnableJpaRepositories("repository")
public class ThuctaptotnghiepApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThuctaptotnghiepApplication.class, args);
	}

}
