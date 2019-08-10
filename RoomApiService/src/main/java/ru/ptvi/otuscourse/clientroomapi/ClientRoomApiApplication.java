package ru.ptvi.otuscourse.clientroomapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@EnableDiscoveryClient
@EnableFeignClients
@EnableIntegration
@IntegrationComponentScan
@SpringBootApplication
public class ClientRoomApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientRoomApiApplication.class, args);
	}

}
