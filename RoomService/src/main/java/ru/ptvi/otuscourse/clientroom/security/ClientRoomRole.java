package ru.ptvi.otuscourse.clientroom.security;

import org.springframework.security.core.GrantedAuthority;

public enum ClientRoomRole implements GrantedAuthority {
    ROLE_CONTRAGENT,
    ROLE_MASTER;

    @Override
    public String getAuthority() {
        return name();
    }
}
