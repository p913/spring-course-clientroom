package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomapi.repository.PayRepository;
import ru.ptvi.otuscourse.clientroomdto.PayDto;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PayApiTest extends BaseApiTest {
    @Autowired
    private PayRepository payRepository;

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/pays должен возвращать массив объектов, содержащих основные атрибуты оплаты")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/pays", CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS)
                .param("dateFrom", "2019-07-02")
                .param("dateTo", "2019-07-02")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].payDateTime").isNotEmpty())
                .andExpect(jsonPath("$[0].summ").isNotEmpty())
                .andExpect(jsonPath("$[0].source").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/pays должен возвращать пустой массив для несуществующего контрагента")
    void shouldBeOkAndEmptyArrayWhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/pays", CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS)
                .param("dateFrom", "2019-07-02")
                .param("dateTo", "2019-07-02")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/pays должен возвращать пустой массив для несуществующего договора")
    void shouldBeOkAndEmptyArrayWhenContractNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/pays", CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS)
                .param("dateFrom", "2019-07-02")
                .param("dateTo", "2019-07-02")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен возвращать объект, содержащий основные атрибуты оплаты")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.payDateTime").isNotEmpty())
                .andExpect(jsonPath("$.summ").isNotEmpty())
                .andExpect(jsonPath("$.source").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен возвращать 404 для несуществующего контрагента")
    void shouldBeStatus404WhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен возвращать 404 для несуществующего договора")
    void shouldBeStatus404WhenContractNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, PAY_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен возвращать 404 для несуществующего оплаты")
    void shouldBeStatus404WhenAccObjectNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/pays должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBeStatus201AndReturnLocationAndId() throws Exception {
        var doc = modelMapper.map(EntityForTest.createPay(null), PayDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/pays", CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(doc)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/contragents/" + CONTRAGENT_ID_EXISTS + "/contracts/" + CONTRACT_ID_EXISTS + "/pays/" )))
                .andReturn();

        assertThat(payRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/pays должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenCreateForNonExistsContragent() throws Exception {
        var doc = modelMapper.map(EntityForTest.createPay(null), PayDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/pays", CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(doc)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/pays должен вернуть 404 для несуществующего догвоора")
    void shouldBeStatus404WhenCreateForNonExistsContract() throws Exception {
        var doc = modelMapper.map(EntityForTest.createPay(null), PayDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/pays", CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(doc)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен обновить источник")
    void shouldBeChangeNameWhenUpdate() throws Exception {
        var source = "New source";
        var pay = payRepository.findById(UUID.fromString(PAY_ID_EXISTS));

        assertThat(pay.isPresent()).isTrue();

        pay.get().setSource(source);

        var dto = modelMapper.map(pay.get(), PayDto.class);

        mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = payRepository.findById(UUID.fromString(PAY_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getSource()).isEqualTo(source);
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenUpdateForNonExistsContragent() throws Exception {
        var doc = modelMapper.map(EntityForTest.createPay(null), PayDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(doc)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен вернуть 404 для несуществующего договора")
    void shouldBeStatus404WhenUpdateForNonExistsContract() throws Exception {
        var doc = modelMapper.map(EntityForTest.createPay(null), PayDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, PAY_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(doc)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} должен вернуть 404 для несуществующего оплаты")
    void shouldBeStatus404WhenUpdateForNonExistsAccObject() throws Exception {
        var doc = modelMapper.map(EntityForTest.createPay(null), PayDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(doc)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Pay", PAY_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDelete() throws Exception {
        var before = payRepository.findById(UUID.fromString(PAY_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_EXISTS))
                .andExpect(status().isNoContent());

        var after = payRepository.findById(UUID.fromString(PAY_ID_EXISTS));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} для несуществующего оплаты должен вернуть статус 304")
    void shouldBeStatus304WhenDelNotExists() throws Exception {
        var before = payRepository.findById(UUID.fromString(PAY_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_NON_EXISTS))
                .andExpect(status().isNotModified());
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} для несуществующего контрагента должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContragentNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, PAY_ID_EXISTS))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/pays/{pid} для несуществующего контрагента должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContractNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/pays/{pid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, PAY_ID_EXISTS))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));

    }



}
