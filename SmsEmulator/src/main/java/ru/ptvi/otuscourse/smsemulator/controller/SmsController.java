package ru.ptvi.otuscourse.smsemulator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
public class SmsController {
    private final RestOperations restClient = new RestTemplate();

    private final String telegramToken;

    private final String telegramChatId;

    public SmsController(@Value("${sms-emulator.telegram.token}") String telegramToken,
                         @Value("${sms-emulator.telegram.chat-id}") String telegramChatId) {
        this.telegramToken = telegramToken;
        this.telegramChatId = telegramChatId;
    }

    @RequestMapping("/send")
    public ResponseEntity send(@RequestParam String phone,
                               @RequestParam String message) throws UnsupportedEncodingException {
        var text = "Смс для *" + phone + "*:\n" + message;
        var url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&parse_mode=markdown&disable_web_page_preview=true&text=%s",
                telegramToken, telegramChatId, text);

        restClient.getForEntity(url, String.class);

        return ResponseEntity.ok().build();
    }
}
