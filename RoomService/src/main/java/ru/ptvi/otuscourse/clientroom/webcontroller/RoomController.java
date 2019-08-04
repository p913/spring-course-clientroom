package ru.ptvi.otuscourse.clientroom.webcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ptvi.otuscourse.clientroom.security.ClientRoomUserDetails;
import ru.ptvi.otuscourse.clientroom.service.RoomService;
import ru.ptvi.otuscourse.clientroom.domain.ChargesBothDto;
import ru.ptvi.otuscourse.clientroom.domain.DocuLinkWithContractDto;
import ru.ptvi.otuscourse.clientroom.domain.PayWithContractDto;
import ru.ptvi.otuscourse.clientroomdto.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    private String getCurrentContragentId() {
        return ((ClientRoomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getContragentId();
    }

    @GetMapping("/contragent")
    public Optional<ContragentWithDetailsDto> getContragentWithContractsWithAccObjects() {
        return roomService.getContragentById(getCurrentContragentId());
    }

    @GetMapping("/charges")
    public ChargesBothDto getCharges(@RequestParam(required = false) String dateFrom,
                                     @RequestParam(required = false) String dateTo) {
        return roomService.getChargesByDatesOrDefault(getCurrentContragentId(), dateFrom, dateTo);
    }

    @GetMapping("/pays")
    public List<PayWithContractDto> getPays(@RequestParam(required = false) String dateFrom,
                                            @RequestParam(required = false) String dateTo) {
        return roomService.getPaysByDatesOrDefault(getCurrentContragentId(), dateFrom, dateTo);
    }

    @GetMapping("/documents")
    public List<DocuLinkWithContractDto> getDocs(@RequestParam(required = false) String dateFrom,
                                                         @RequestParam(required = false) String dateTo) {
        return roomService.getDocsByDatesOrDefault(getCurrentContragentId(), dateFrom, dateTo);
    }


    @GetMapping("/notifications")
    public List<NotificationDto> getNotifications(@RequestParam(required = false) String dateFrom,
                                                  @RequestParam(required = false) String dateTo) {
        if (dateFrom == null || dateFrom.isEmpty() || dateTo == null || dateTo.isEmpty())
            return roomService.getNewNotifications(getCurrentContragentId());
        else
            return roomService.getNotificationsByDatesOrEmpty(getCurrentContragentId(), dateFrom, dateTo);
    }

    @PutMapping("/notifications")
    public void markNotificationsAsViewed(@RequestBody String[] notifications) {
        roomService.markNotificationAsViewed(getCurrentContragentId(), notifications);
    }

    @GetMapping("/demands")
    public List<DemandDto> getDemands(@RequestParam(required = false) String dateFrom,
                                      @RequestParam(required = false) String dateTo) {
        return roomService.getDemandsByDatesOrDefault(getCurrentContragentId(), dateFrom, dateTo);
    }

    @PostMapping("/demand")
    public ResponseEntity pushDemand(@RequestBody DemandDto demand) {
        return ResponseEntity.created(URI.create(roomService.pushDemand(getCurrentContragentId(), demand))).build();
    }

}
