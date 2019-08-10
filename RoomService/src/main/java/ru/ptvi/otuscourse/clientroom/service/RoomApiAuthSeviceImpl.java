package ru.ptvi.otuscourse.clientroom.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import ru.ptvi.otuscourse.clientroom.config.RoomConfigProps;

@RefreshScope
@Service
@Slf4j
public class RoomApiAuthSeviceImpl implements RoomApiAuthSevice {
    private String header;

    public RoomApiAuthSeviceImpl(RoomConfigProps props) {
        this.header = "Basic " + Base64Utils.encodeToString((props.getSecurity().getApi().getUsername() + ":"
                + props.getSecurity().getApi().getPassword()).getBytes());
        //log.info("Credentials for API: " + apiUserName + ", " + apiPassword);
    }

    @Override
    public String getAuthHeader() {
        return header;
    }
}
