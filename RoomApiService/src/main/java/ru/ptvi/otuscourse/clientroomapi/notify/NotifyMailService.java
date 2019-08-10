package ru.ptvi.otuscourse.clientroomapi.notify;

public interface NotifyMailService {
    void send(String email, String message);
}
