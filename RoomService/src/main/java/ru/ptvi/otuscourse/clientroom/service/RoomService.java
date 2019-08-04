package ru.ptvi.otuscourse.clientroom.service;

import ru.ptvi.otuscourse.clientroom.domain.ChargesBothDto;
import ru.ptvi.otuscourse.clientroom.domain.DocuLinkWithContractDto;
import ru.ptvi.otuscourse.clientroom.domain.PayWithContractDto;
import ru.ptvi.otuscourse.clientroomdto.*;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    Optional<ContragentWithDetailsDto> getContragentById(String id);

    Optional<ContragentWithDetailsDto> getContragentByEmailOrPhone(String param);

    List<DocuLinkWithContractDto> getDocsByDatesOrDefault(String contragentId, String dateFrom, String dateTo);

    List<NotificationDto> getNewNotifications(String contragentId);

    List<NotificationDto> getNotificationsByDatesOrEmpty(String contragentId, String dateFrom, String dateTo);

    void markNotificationAsViewed(String contragentId, String[] notifications);

    List<PayWithContractDto> getPaysByDatesOrDefault(String contragentId, String dateFrom, String dateTo);

    ChargesBothDto getChargesByDatesOrDefault(String contragentId, String dateFrom, String dateTo);

    List<DemandDto> getDemandsByDatesOrDefault(String contragentId, String dateFrom, String dateTo);

    String pushDemand(String contragentId, DemandDto demand);
}
