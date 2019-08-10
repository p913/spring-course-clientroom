package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.ptvi.otuscourse.clientroomapi.domain.Service;
import ru.ptvi.otuscourse.clientroomapi.repository.AccountingObjectRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccObjectApiTest extends BaseApiTest {
    @Autowired
    private AccountingObjectRepository accountingObjectRepository;

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects должен возвращать массив объектов, содержащих основные атрибуты объектов учета")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects", CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].dateFrom").isNotEmpty())
                .andExpect(jsonPath("$[0].service.id").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects должен возвращать пустой массив для несуществующего контрагента")
    void shouldBeOkAndEmptyArrayWhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects", CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects должен возвращать пустой массив для несуществующего договора")
    void shouldBeOkAndEmptyArrayWhenContractNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects", CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен возвращать объект, содержащий основные атрибуты объекта учета")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                            CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.dateFrom").isNotEmpty())
                .andExpect(jsonPath("$.service.id").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен возвращать 404 для несуществующего контрагента")
    void shouldBeStatus404WhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                            CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен возвращать 404 для несуществующего договора")
    void shouldBeStatus404WhenContractNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                            CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен возвращать 404 для несуществующего объекта учета")
    void shouldBeStatus404WhenAccObjectNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                            CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/accobjects должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBeStatus201AndReturnLocationAndId() throws Exception {
        var accOvject = modelMapper.map(EntityForTest.createAccountingObject(null,
                new Service(UUID.fromString(SERVICE_ID_EXISTS))),
                AccountingObjectDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/accobjects", CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accOvject)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/contragents/" + CONTRAGENT_ID_EXISTS + "/contracts/" + CONTRACT_ID_EXISTS + "/accobjects/" )))
                .andReturn();

        assertThat(accountingObjectRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/accobjects должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenCreateForNonExistsContragent() throws Exception {
        var accOvject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())), AccountingObjectDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/accobjects", CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accOvject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/accobjects должен вернуть 404 для несуществующего догвоора")
    void shouldBeStatus404WhenCreateForNonExistsContract() throws Exception {
        var accOvject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())), AccountingObjectDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/accobjects", CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accOvject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен обновить наименование")
    void shouldBeChangeNameWhenUpdate() throws Exception {
        var name = "New name";
        var accObject = accountingObjectRepository.findById(UUID.fromString(ACCOBJECT_ID_EXISTS));

        assertThat(accObject.isPresent()).isTrue();

        accObject.get().setName(name);

        var dto = modelMapper.map(accObject.get(), AccountingObjectDto.class);

        mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = accountingObjectRepository.findById(UUID.fromString(ACCOBJECT_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenUpdateForNonExistsContragent() throws Exception {
        var accObject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())),
                AccountingObjectDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                    CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accObject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен вернуть 404 для несуществующего договора")
    void shouldBeStatus404WhenUpdateForNonExistsContract() throws Exception {
        var accObject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())),
                AccountingObjectDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accObject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} должен вернуть 404 для несуществующего объекта учета")
    void shouldBeStatus404WhenUpdateForNonExistsAccObject() throws Exception {
        var accObject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())),
                AccountingObjectDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accObject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Accounting object", ACCOBJECT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDelete() throws Exception {
        var before = accountingObjectRepository.findById(UUID.fromString(ACCOBJECT_ID_WITHOUT_REFERENCES));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_WITHOUT_REFERENCES))
                .andExpect(status().isNoContent());

        var after = accountingObjectRepository.findById(UUID.fromString(ACCOBJECT_ID_WITHOUT_REFERENCES));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} для несуществующего объекта должен вернуть статус 304")
    void shouldBeStatus304WhenDelNotExists() throws Exception {
        var before = accountingObjectRepository.findById(UUID.fromString(ACCOBJECT_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS))
                .andExpect(status().isNotModified());
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} для несуществующего контрагента должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContragentNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_WITHOUT_REFERENCES))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                    stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} для несуществующего договора должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContractNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_WITHOUT_REFERENCES))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));

    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid} для имеющего в зависимостях начисления должен вернуть статус 409")
    void shouldBeStatus409WhenHasReferences() throws Exception {
        var before = accountingObjectRepository.findById(UUID.fromString(ACCOBJECT_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS))
                .andExpect(status().isConflict());
    }


}
