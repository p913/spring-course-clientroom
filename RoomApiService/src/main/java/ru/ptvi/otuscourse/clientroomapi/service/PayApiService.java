package ru.ptvi.otuscourse.clientroomapi.service;

import ru.ptvi.otuscourse.clientroomdto.PayDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayApiService {
    List<PayDto> getBetweenDates(String contragentId, String contractId, LocalDate from, LocalDate to);

    Optional<PayDto> getById(String contragentId, String contractId, String payId);

    void create(String contragentId, String contractId, PayDto payDto);

    void update(String contragentId, String contractId, PayDto payDto);

    boolean delete(String contragentId, String contractId, String payId);
}
