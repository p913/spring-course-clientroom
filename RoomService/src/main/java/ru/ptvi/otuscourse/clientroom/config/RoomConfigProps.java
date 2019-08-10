package ru.ptvi.otuscourse.clientroom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("room")
public class RoomConfigProps {

    private String company;

    private String contacts;

    private Security security = new Security();

    @Data
    public static class Security {
        private SecurityCredentials api = new SecurityCredentials();

        private SecurityCredentials master = new SecurityCredentials();

    }

    @Data
    public static class SecurityCredentials {
        private String username;

        private String password;
    }
}
