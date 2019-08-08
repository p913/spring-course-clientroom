package ru.ptvi.otuscourse.clientroomapi.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotifyMailServiceImpl implements NotifyMailService {
    private final JavaMailSender javaMailSender;

    private final String subject;

    private final String from;

    public NotifyMailServiceImpl(JavaMailSender javaMailSender,
                                 @Value("${room-api.mail.from}") String from,
                                 @Value("${room-api.mail.subject}") String subject) {
        this.javaMailSender = javaMailSender;
        this.from = from;
        this.subject = subject;
    }

    @Override
    public void send(String email, String message) {
        log.info("Will send mail to " + email);

        var msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject(subject);
        msg.setText(message);
        msg.setFrom(from);

        javaMailSender.send(msg);
    }
}
