package com.shopme.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity"})
public class AdminSideApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminSideApplication.class, args);
	}

}