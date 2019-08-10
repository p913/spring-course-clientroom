package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import ru.ptvi.otuscourse.clientroomapi.repository.DemandRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;
import ru.ptvi.otuscourse.clientroomdto.ContractDto;
import ru.ptvi.otuscourse.clientroomdto.DemandDto;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DemandApiTest extends BaseApiTest {
    @Autowired
    private DemandRepository demandRepository;

    @Test
    @DisplayName("GET /api/contragents/{cgid}/demands должен возвращать массив объектов, содержащих основные атрибуты заявки")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/demands", CONTRAGENT_ID_EXISTS)
                .param("dateFrom", LocalDate.now().toString())
                .param("dateTo", LocalDate.now().toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].contract").hasJsonPath())
                .andExpect(jsonPath("$[0].accObject").hasJsonPath())
                .andExpect(jsonPath("$[0].decisionDateTime").hasJsonPath())
                .andExpect(jsonPath("$[0].demandDateTime").isNotEmpty())
                .andExpect(jsonPath("$[0].demandSubject").isNotEmpty())
                .andExpect(jsonPath("$[0].demandNote").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/demands должен возвращать пустой массив для несуществующего контрагента")
    void shouldBeOkAndEmptyArrayWhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/demands", CONTRAGENT_ID_NON_EXISTS)
                .param("dateFrom", LocalDate.now().toString())
                .param("dateTo", LocalDate.now().toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/demands/{dmid} должен возвращать объект, содержащий основные атрибуты заявки")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_EXISTS , DEMAND_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.contract").hasJsonPath())
                .andExpect(jsonPath("$.accObject").hasJsonPath())
                .andExpect(jsonPath("$.decisionDateTime").hasJsonPath())
                .andExpect(jsonPath("$.demandDateTime").isNotEmpty())
                .andExpect(jsonPath("$.demandSubject").isNotEmpty())
                .andExpect(jsonPath("$.demandNote").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/demands/{dmid} должен возвращать 404 для несуществующего контрагента")
    void shouldBeStatus404WhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_NON_EXISTS, DEMAND_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/demands/{dmid} должен возвращать 404 для несуществующего заявки")
    void shouldBeStatus404WhenDemandNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_EXISTS, DEMAND_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/demands должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBeStatus201AndReturnLocationAndId() throws Exception {
        var demand = modelMapper.map(EntityForTest.createDemand(null, null, null), DemandDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/demands", CONTRAGENT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(demand)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/contragents/" + CONTRAGENT_ID_EXISTS + "/demands/")))
                .andReturn();

        assertThat(demandRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/demands должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenCreateForNonExistsContragent() throws Exception {
        var demand = modelMapper.map(EntityForTest.createDemand(null, null, null), DemandDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/demands", CONTRAGENT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(demand)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/demands должен вернуть 404 для несуществующего договора (внутри заявки)")
    void shouldBeStatus404WhenCreateForNonExistsContract() throws Exception {
        var demand = modelMapper.map(EntityForTest.createDemand(null, null, null), DemandDto.class);
        demand.contract(new ContractDto().id(UUID.randomUUID().toString()));

        var res = mvc.perform(post("/api/contragents/{cgid}/demands", CONTRAGENT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(demand)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", demand.contract().id()))));
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/demands должен вернуть 404 для несуществующего объекта учета (внутри заявки)")
    void shouldBeStatus404WhenCreateForNonExistsAccObject() throws Exception {
        var demand = modelMapper.map(EntityForTest.createDemand(null, null, null), DemandDto.class);
        demand.accObject(new AccountingObjectDto().id(UUID.randomUUID().toString()));

        var res = mvc.perform(post("/api/contragents/{cgid}/demands", CONTRAGENT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(demand)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Accounting object", demand.accObject().id()))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/demands/{dmid} должен обновить баланс")
    void shouldBeChangeBalanceWhenUpdate() throws Exception {
        var message = "New message";
        var demand = demandRepository.findById(UUID.fromString(DEMAND_ID_EXISTS));

        assertThat(demand.isPresent()).isTrue();

        demand.get().setDemandNote(message);

        var dto = modelMapper.map(demand.get(), DemandDto.class);

        mvc.perform(put("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_EXISTS, DEMAND_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = demandRepository.findById(UUID.fromString(DEMAND_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getDemandNote()).isEqualTo(message);
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/demands/{dmid} должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenUpdateForNonExistsContragent() throws Exception {
        var demand = modelMapper.map(EntityForTest.createNotification(null), DemandDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_NON_EXISTS, DEMAND_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(demand)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/demands/{dmid} должен вернуть 404 для существующего контрагента но несуществующего заявки")
    void shouldBeStatus404WhenUpdateForNonExistsDemand() throws Exception {
        var demand = modelMapper.map(EntityForTest.createNotification(null), DemandDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_EXISTS, DEMAND_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(demand)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Demand", DEMAND_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/demands/{dmid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDeleteWhenDel() throws Exception {
        var before = demandRepository.findById(UUID.fromString(DEMAND_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_EXISTS, DEMAND_ID_EXISTS))
                .andExpect(status().isNoContent());

        var after = demandRepository.findById(UUID.fromString(DEMAND_ID_EXISTS));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/demands/{dmid} для несуществующего должен вернуть статус 304")
    void shouldBeStatus304WhenDelDemandNotExists() throws Exception {
        var before = demandRepository.findById(UUID.fromString(DEMAND_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_EXISTS, DEMAND_ID_NON_EXISTS))
                .andExpect(status().isNotModified());

    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/demands/{dmid} для несуществующего контрагента должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContragentNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/demands/{dmid}", CONTRAGENT_ID_NON_EXISTS, DEMAND_ID_EXISTS))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }


}
