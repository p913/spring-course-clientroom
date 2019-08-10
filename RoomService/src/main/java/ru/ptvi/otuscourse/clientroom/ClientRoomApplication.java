package ru.ptvi.otuscourse.clientroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.ptvi.otuscourse.clientroom.config.RoomConfigProps;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties(RoomConfigProps.class)
public class ClientRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientRoomApplication.class, args);
	}

}
