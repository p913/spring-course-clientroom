package ru.ptvi.otuscourse.clientroomapi.service;

import ru.ptvi.otuscourse.clientroomdto.ChargeOnceDto;
import ru.ptvi.otuscourse.clientroomdto.ChargeServiceDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChargesApiService {

    List<ChargeOnceDto> getChargeOnceBetweenDates(String contragentId, String contractId, LocalDate from, LocalDate to);

    Optional<ChargeOnceDto> getChargeOnceById(String contragentId, String contractId, String chargeId);

    void createChargeOnce(String contragentId, String contractId, ChargeOnceDto chargeDto);

    void updateChargeOnce(String contragentId, String contractId, ChargeOnceDto chargeDto);

    boolean deleteChargeOnce(String contragentId, String contractId, String chargeId);


    List<ChargeServiceDto> getChargeServiceBetweenDates(String contragentId, String contractId, String accObjectId, LocalDate from, LocalDate to);

    Optional<ChargeServiceDto> getChargeServiceById(String contragentId, String contractId, String accObjectId, String chargeId);

    void createChargeService(String contragentId, String contractId, String accObjectId, ChargeServiceDto chargeDto);

    void updateChargeService(String contragentId, String contractId, String accObjectId, ChargeServiceDto chargeDto);

    boolean deleteChargeService(String contragentId, String contractId, String accObjectId, String chargeId);

}
