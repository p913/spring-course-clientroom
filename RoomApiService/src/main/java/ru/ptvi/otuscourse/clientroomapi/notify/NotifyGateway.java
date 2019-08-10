package ru.ptvi.otuscourse.clientroomapi.notify;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface NotifyGateway {
    @Gateway(requestChannel = "notifyClientsChannel")
    void send(NotifyMessagePayload messagePayload);
}
