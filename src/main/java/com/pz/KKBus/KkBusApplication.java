package com.pz.KKBus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class KkBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(KkBusApplication.class, args);
	}
}
