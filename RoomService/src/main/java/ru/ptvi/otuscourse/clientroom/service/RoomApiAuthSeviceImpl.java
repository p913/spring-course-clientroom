package ru.ptvi.otuscourse.clientroom.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@RefreshScope
@Service
@Slf4j
public class RoomApiAuthSeviceImpl implements RoomApiAuthSevice {
    private String header;

    public RoomApiAuthSeviceImpl(@Value("${room.security.api.username}") String apiUserName,
                                 @Value("${room.security.api.password}") String apiPassword) {
        this.header = "Basic " + Base64Utils.encodeToString((apiUserName + ":" + apiPassword).getBytes());
        log.info("Credentials for API: " + apiUserName + ", " + apiPassword);
    }

    @Override
    public String getAuthHeader() {
        return header;
    }
}
