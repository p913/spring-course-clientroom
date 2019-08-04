package ru.ptvi.otuscourse.clientroom.security;

import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class ClientRoomUserDetails extends User {
    private final String contragentId;


    public ClientRoomUserDetails(String username, String password, String contragentId) {
        super(username, password, Collections.singleton(ClientRoomRole.ROLE_CONTRAGENT));
        this.contragentId = contragentId;
    }

    public ClientRoomUserDetails(String username, String password) {
        super(username, password, Collections.singleton(ClientRoomRole.ROLE_MASTER));
        this.contragentId = "";
    }

    public String getContragentId() {
        return contragentId;
    }
}
