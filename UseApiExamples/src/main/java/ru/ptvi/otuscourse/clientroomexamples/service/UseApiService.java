package ru.ptvi.otuscourse.clientroomexamples.service;

import ru.ptvi.otuscourse.clientroomdto.ContragentWithDetailsDto;
import ru.ptvi.otuscourse.clientroomdto.DemandDto;

import java.util.List;
import java.util.Optional;

public interface UseApiService {
    String createContragentAsPeople(String email, String phone, String firstName, String middleName, String lastName,
                                    String contract, String address);

    Optional<ContragentWithDetailsDto> getContragentById(String id);

    List<ContragentWithDetailsDto> getContragentsByEmailOrPhone(String account);

    void chargeAndNotify(String id, int year, int month);

    List<DemandDto> getContragentOpenedDemands(String id);

    void closeDemand(String contragentId, String demandId, boolean success, String message);
}
