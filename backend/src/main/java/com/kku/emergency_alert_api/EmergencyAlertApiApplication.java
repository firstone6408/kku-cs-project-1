package com.kku.emergency_alert_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmergencyAlertApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergencyAlertApiApplication.class, args);

		System.out.println(" - Reloaded Success");
	}

}
