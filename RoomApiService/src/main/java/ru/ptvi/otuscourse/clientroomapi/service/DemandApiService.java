package ru.ptvi.otuscourse.clientroomapi.service;

import ru.ptvi.otuscourse.clientroomdto.DemandDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DemandApiService {
    List<DemandDto> getBetweenDates(String contragentId, LocalDate from, LocalDate to);

    Optional<DemandDto> getById(String contragentId, String demandId);

    void create(String contragentId, DemandDto demandDto);

    void update(String contragentId, DemandDto demandDto);

    boolean delete(String contragentId, String demandId);
}
