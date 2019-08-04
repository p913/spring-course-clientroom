package ru.ptvi.otuscourse.clientroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ClientRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientRoomApplication.class, args);
	}

}
