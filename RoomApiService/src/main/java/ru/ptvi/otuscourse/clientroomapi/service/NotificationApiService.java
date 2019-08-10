package ru.ptvi.otuscourse.clientroomapi.service;

import ru.ptvi.otuscourse.clientroomdto.NotificationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationApiService {
    List<NotificationDto> getBetweenDates(String contragentId, LocalDate from, LocalDate to);

    List<NotificationDto> getUnviewed(String contragentId);

    Optional<NotificationDto> getById(String contragentId, String notId);

    void create(String contragentId, NotificationDto notDto);

    void update(String contragentId, NotificationDto notDto);

    boolean delete(String contragentId, String notId);
}
