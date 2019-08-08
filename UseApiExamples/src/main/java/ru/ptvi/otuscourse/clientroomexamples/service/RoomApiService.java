package ru.ptvi.otuscourse.clientroomexamples.service;

import feign.Contract;
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

    @GetMapping("/api/contragents")
    List<ContragentWithDetailsDto> getContragentsByEmailOrPhone(@RequestHeader("Authorization") String auth,
                                                                @RequestParam String account,
                                                                @RequestParam boolean details);

    @PostMapping("/api/contragents")
    String createContragent(@RequestHeader("Authorization") String auth,
                            @RequestBody ContragentDto contragentDto);

    @PostMapping("/api/contragents/{cgid}/contracts")
    String createContract(@RequestHeader("Authorization") String auth,
                          @PathVariable String cgid,
                          @RequestBody ContractDto contractDto);

    @PutMapping("/api/contragents/{cgid}/contracts/{ctid}")
    String updateContract(@RequestHeader("Authorization") String auth,
                          @PathVariable String cgid,
                          @PathVariable String ctid,
                          @RequestBody ContractDto contractDto);

    @PostMapping("/api/contragents/{cgid}/contracts/{ctid}/accobjects")
    String createAccObject(@RequestHeader("Authorization") String auth,
                           @PathVariable String cgid,
                           @PathVariable String ctid,
                           @RequestBody AccountingObjectDto accountingObjectDto);

    @GetMapping("/api/services")
    List<ServiceDto> getAllServices(@RequestHeader("Authorization") String auth);

    @GetMapping("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges")
    List<ChargeServiceDto> getServiceCharges(@RequestHeader("Authorization") String auth,
                                             @PathVariable String cgid,
                                             @PathVariable String ctid,
                                             @PathVariable String aoid,
                                             @RequestParam String dateFrom,
                                             @RequestParam String dateTo);

    @PostMapping("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges")
    String createServiceCharges(@RequestHeader("Authorization") String auth,
                                @PathVariable String cgid,
                                @PathVariable String ctid,
                                @PathVariable String aoid,
                                @RequestBody ChargeServiceDto chargeServiceDto);

    @DeleteMapping("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}")
    void deleteServiceCharges(@RequestHeader("Authorization") String auth,
                              @PathVariable String cgid,
                              @PathVariable String ctid,
                              @PathVariable String aoid,
                              @PathVariable String chid);

    @PostMapping("/api/contragents/{cgid}/notifications")
    String createNotification(@RequestHeader("Authorization") String auth,
                              @PathVariable String cgid,
                              @RequestBody NotificationDto notificationDto);

}
