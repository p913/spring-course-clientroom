package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomapi.repository.NotificationRepository;
import ru.ptvi.otuscourse.clientroomdto.NotificationDto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NotificationApiTest extends BaseApiTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("GET /api/contragents/{cgid}/notifications должен возвращать массив объектов, содержащих основные атрибуты уведомления")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/notifications", CONTRAGENT_ID_EXISTS)
                    .param("dateFrom", LocalDate.now().toString())
                    .param("dateTo", LocalDate.now().toString())
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].originDateTime").isNotEmpty())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].sendEmail").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/notifications должен возвращать пустой массив для несуществующего контрагента")
    void shouldBeOkAndEmptyArrayWhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/notifications", CONTRAGENT_ID_NON_EXISTS)
                    .param("dateFrom", LocalDate.now().toString())
                    .param("dateTo", LocalDate.now().toString())
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/notifications/{nid} должен возвращать объект, содержащий основные атрибуты уведомления")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_EXISTS , NOTIFICATION_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.originDateTime").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.sendEmail").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/notifications/{nid} должен возвращать 404 для несуществующего контрагента")
    void shouldBeStatus404WhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_NON_EXISTS, NOTIFICATION_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/notifications/{nid} должен возвращать 404 для несуществующего уведомления")
    void shouldBeStatus404WhenNotificationNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_EXISTS, NOTIFICATION_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/notifications должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBeStatus201AndReturnLocationAndId() throws Exception {
        var notif = modelMapper.map(EntityForTest.createNotification(null), NotificationDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/notifications", CONTRAGENT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(notif)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/contragents/" + CONTRAGENT_ID_EXISTS + "/notifications/")))
                .andReturn();

        assertThat(notificationRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/notifications должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenCreateForNonExistsContragent() throws Exception {
        var notif = modelMapper.map(EntityForTest.createNotification(null), NotificationDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/notifications", CONTRAGENT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(notif)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }


    @Test
    @DisplayName("PUT /api/contragents/{cgid}/notifications/{nid} должен обновить баланс")
    void shouldBeChangeBalanceWhenUpdate() throws Exception {
        var message = "New message";
        var notification = notificationRepository.findById(UUID.fromString(NOTIFICATION_ID_EXISTS));

        assertThat(notification.isPresent()).isTrue();

        notification.get().setMessage(message);

        var dto = modelMapper.map(notification.get(), NotificationDto.class);

        mvc.perform(put("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_EXISTS, NOTIFICATION_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = notificationRepository.findById(UUID.fromString(NOTIFICATION_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/notifications/{nid} должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenUpdateForNonExistsContragent() throws Exception {
        var notif = modelMapper.map(EntityForTest.createNotification(null), NotificationDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_NON_EXISTS, NOTIFICATION_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(notif)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/notifications/{nid} должен вернуть 404 для существующего контрагента но несуществующего уведомления")
    void shouldBeStatus404WhenUpdateForNonExistsNotification() throws Exception {
        var notif = modelMapper.map(EntityForTest.createNotification(null), NotificationDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_EXISTS, NOTIFICATION_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(notif)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Notification", NOTIFICATION_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/notifications/{nid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDeleteWhenDel() throws Exception {
        var before = notificationRepository.findById(UUID.fromString(NOTIFICATION_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_EXISTS, NOTIFICATION_ID_EXISTS))
                .andExpect(status().isNoContent());

        var after = notificationRepository.findById(UUID.fromString(NOTIFICATION_ID_EXISTS));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/notifications/{nid} для несуществующего должен вернуть статус 304")
    void shouldBeStatus304WhenDelNotificationNotExists() throws Exception {
        var before = notificationRepository.findById(UUID.fromString(NOTIFICATION_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_EXISTS, NOTIFICATION_ID_NON_EXISTS))
                .andExpect(status().isNotModified());

    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/notifications/{nid} для несуществующего контрагента должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContragentNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/notifications/{nid}", CONTRAGENT_ID_NON_EXISTS, NOTIFICATION_ID_EXISTS))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

}
