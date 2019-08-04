package ru.ptvi.otuscourse.clientroom.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.ptvi.otuscourse.clientroomdto.*;

import java.util.List;
import java.util.Optional;

@FeignClient("room-api-microservice")
public interface RoomApiService {
    @GetMapping("/api/contragents/{cgid}")
    Optional<ContragentWithDetailsDto> getContragentById(@RequestHeader("Authorization") String auth,
                                              @PathVariable String cgid);

    @PatchMapping("/api/contragents/{cgid}")
    void patchContragent(@RequestHeader("Authorization") String auth,
                         @PathVariable String cgid,
                         @RequestBody ContragentDto contragentDto);

    @GetMapping("/api/contragents/")
    List<ContragentWithDetailsDto> getContragentByEmailOrPhone(@RequestHeader("Authorization") String auth,
                                                    @RequestParam String account,
                                                    @RequestParam boolean details);

    @GetMapping("/api/contragents/{cgid}/contracts")
    List<ContractDto> getContragentContracts(@RequestHeader("Authorization") String auth,
                                             @PathVariable("cgid") String contragentId);

    @GetMapping("/api/contragents/{cgid}/contracts/{ctid}/documents")
    List<DocuLinkDto> getContractDocuments(@RequestHeader("Authorization") String auth,
                                           @PathVariable("cgid") String contragentId,
                                           @PathVariable("ctid") String contractId,
                                           @RequestParam String dateFrom,
                                           @RequestParam String dateTo);

    @GetMapping("/api/contragents/{cgid}/contracts/{ctid}/pays")
    List<PayDto> getContractPays(@RequestHeader("Authorization") String auth,
                                 @PathVariable("cgid") String contragentId,
                                 @PathVariable("ctid") String contractId,
                                 @RequestParam String dateFrom,
                                 @RequestParam String dateTo);

    @GetMapping("/api/contragents/{cgid}/notifications")
    List<NotificationDto> getContragentNotifications(@RequestHeader("Authorization") String auth,
                                                     @PathVariable("cgid") String contragentId,
                                                     @RequestParam String dateFrom,
                                                     @RequestParam String dateTo);

    @GetMapping("/api/contragents/{cgid}/notifications/{nid}")
    Optional<NotificationDto> getNotificationById(@RequestHeader("Authorization") String auth,
                                                  @PathVariable("cgid") String contragentId,
                                                  @PathVariable("nid") String notificationId);

    @PutMapping("/api/contragents/{cgid}/notifications/{nid}")
    void updateNotification(@RequestHeader("Authorization") String auth,
                            @PathVariable("cgid") String contragentId,
                            @PathVariable("nid") String notificationId,
                            @RequestBody NotificationDto notification);

    @GetMapping("/api/contragents/{cgid}/demands")
    List<DemandDto> getContragentDemands(@RequestHeader("Authorization") String auth,
                                                     @PathVariable("cgid") String contragentId,
                                                     @RequestParam String dateFrom,
                                                     @RequestParam String dateTo);

    @PostMapping("/api/contragents/{cgid}/demands")
    String createDemand(@RequestHeader("Authorization") String auth,
                            @PathVariable("cgid") String contragentId,
                            @RequestBody DemandDto demand);

    @GetMapping("/api/contragents/{cgid}/contracts/{ctid}/charges")
    List<ChargeOnceDto> getContractCharges(@RequestHeader("Authorization") String auth,
                                 @PathVariable("cgid") String contragentId,
                                 @PathVariable("ctid") String contractId,
                                 @RequestParam String dateFrom,
                                 @RequestParam String dateTo);

    @GetMapping("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges")
    List<ChargeServiceDto> getAccObjectCharges(@RequestHeader("Authorization") String auth,
                                           @PathVariable("cgid") String contragentId,
                                           @PathVariable("ctid") String contractId,
                                           @PathVariable("aoid") String accObjectId,
                                           @RequestParam String dateFrom,
                                           @RequestParam String dateTo);

}
