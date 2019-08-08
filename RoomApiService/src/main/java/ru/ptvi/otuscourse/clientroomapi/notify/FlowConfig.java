package ru.ptvi.otuscourse.clientroomapi.notify;

import liquibase.util.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class FlowConfig {

    private final NotifyMailService notifyMailService;

    private final NotifySmsService notifySmsService;

    public FlowConfig(NotifyMailService notifyMailService, NotifySmsService notifySmsService) {
        this.notifyMailService = notifyMailService;
        this.notifySmsService = notifySmsService;
    }

    @Bean
    public PublishSubscribeChannel notifyClientsChannel() {
        var t = new ThreadPoolTaskExecutor();
        t.initialize();

        return MessageChannels
                .publishSubscribe(t)
                .get();
    }

    @Bean
    public IntegrationFlow notifyMailFlow() {
        return IntegrationFlows.from("notifyClientsChannel")
                .<NotifyMessagePayload>filter(m -> m.sendEmail())
                .<NotifyMessagePayload>handle((p, h) -> {notifyMailService.send(p.email(), p.message()); return null; })
                .get();
    }

    @Bean
    public IntegrationFlow notifySmsFlow() {
        return IntegrationFlows.from("notifyClientsChannel")
                .<NotifyMessagePayload>filter(m -> m.sendSms() && !StringUtils.isEmpty(m.phone()))
                .<NotifyMessagePayload>handle((p, h) -> {notifySmsService.send(p.phone(), p.message()); return null; })
                .get();
    }

}
