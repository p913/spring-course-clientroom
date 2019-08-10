package ru.ptvi.otuscourse.clientroomapi.service;

import ru.ptvi.otuscourse.clientroomdto.ServiceDto;

import java.util.List;
import java.util.Optional;

public interface ServiceApiService {
    List<ServiceDto> getAll();

    Optional<ServiceDto> getById(String serviceId);

    void create(ServiceDto serviceDto);

    void update(ServiceDto serviceDto);

    boolean delete(String serviceId);
}
