package ru.ptvi.otuscourse.clientroomapi.notify;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("sms-microservice")
public interface NotifySmsService {
    @PostMapping("/send")
    public void send(@RequestParam String phone,
                     @RequestParam String message);
}
