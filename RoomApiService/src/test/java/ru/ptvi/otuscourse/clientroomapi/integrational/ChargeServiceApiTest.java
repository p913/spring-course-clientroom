package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.ptvi.otuscourse.clientroomapi.domain.AccountingObject;
import ru.ptvi.otuscourse.clientroomapi.domain.Service;
import ru.ptvi.otuscourse.clientroomapi.repository.ChargeServiceRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;
import ru.ptvi.otuscourse.clientroomdto.ChargeServiceDto;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChargeServiceApiTest extends BaseApiTest {
    @Autowired
    private ChargeServiceRepository chargeServiceRepository;

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен возвращать массив объектов, содержащих основные атрибуты начислений")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                    .param("dateFrom", "2019-07-01")
                    .param("dateTo", "2019-07-01")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].service.id").isNotEmpty())
                .andExpect(jsonPath("$[0].dateFrom").isNotEmpty())
                .andExpect(jsonPath("$[0].summ").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен возвращать пустой массив для несуществующего контрагента")
    void shouldBeOkAndEmptyArrayWhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                    CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                    .param("dateFrom", "2019-07-01")
                    .param("dateTo", "2019-07-01")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен возвращать пустой массив для несуществующего договора")
    void shouldBeOkAndEmptyArrayWhenContractNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_EXISTS)
                    .param("dateFrom", "2019-07-01")
                    .param("dateTo", "2019-07-01")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен возвращать пустой массив для несуществующего объекта учета")
    void shouldBeOkAndEmptyArrayWhenAccObjectNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS)
                    .param("dateFrom", "2019-07-01")
                    .param("dateTo", "2019-07-01")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }




    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен возвращать объект, содержащий основные атрибуты начисления")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.service.id").isNotEmpty())
                .andExpect(jsonPath("$.dateFrom").isNotEmpty())
                .andExpect(jsonPath("$.summ").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен возвращать 404 для несуществующего контрагента")
    void shouldBeStatus404WhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен возвращать 404 для несуществующего договора")
    void shouldBeStatus404WhenContractNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен возвращать 404 для несуществующего объекта учета")
    void shouldBeStatus404WhenAccObjectNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен возвращать 404 для несуществующего начисления")
    void shouldBeStatus404WhenChargeNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBeStatus201AndReturnLocationAndId() throws Exception {
        var charge = modelMapper.map(EntityForTest.createChargeService(
                    new AccountingObject(UUID.fromString(ACCOBJECT_ID_EXISTS)),
                    new Service(UUID.fromString(SERVICE_ID_EXISTS))),
                ChargeServiceDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(charge)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/contragents/" + CONTRAGENT_ID_EXISTS + "/contracts/" +
                        CONTRACT_ID_EXISTS + "/accobjects/" + ACCOBJECT_ID_EXISTS + "/charges/")))
                .andReturn();

        assertThat(chargeServiceRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenCreateForNonExistsContragent() throws Exception {
        var accOvject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())), AccountingObjectDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                    CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accOvject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен вернуть 404 для несуществующего догвоора")
    void shouldBeStatus404WhenCreateForNonExistsContract() throws Exception {
        var accOvject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())), AccountingObjectDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accOvject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges должен вернуть 404 для несуществующего объекта учета")
    void shouldBeStatus404WhenCreateForNonExistsAccObject() throws Exception {
        var accOvject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())), AccountingObjectDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accOvject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Accounting object", ACCOBJECT_ID_NON_EXISTS))));
    }



    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен обновить усдугу")
    void shouldBeChangeNameWhenUpdate() throws Exception {
        var service = new Service(UUID.fromString(SERVICE_ID_WITHOUT_REFERENCES));
        var charge = chargeServiceRepository.findById(UUID.fromString(CHARGE_SERVICE_ID_EXISTS));

        assertThat(charge.isPresent()).isTrue();

        charge.get().setService(service);

        var dto = modelMapper.map(charge.get(), ChargeServiceDto.class);

        mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = chargeServiceRepository.findById(UUID.fromString(CHARGE_SERVICE_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getService().getId()).isEqualTo(service.getId());
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenUpdateForNonExistsContragent() throws Exception {
        var accObject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())),
                AccountingObjectDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accObject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен вернуть 404 для несуществующего договора")
    void shouldBeStatus404WhenUpdateForNonExistsContract() throws Exception {
        var accObject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())),
                AccountingObjectDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accObject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен вернуть 404 для несуществующего объекта учета")
    void shouldBeStatus404WhenUpdateForNonExistsAccObject() throws Exception {
        var accObject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())),
                AccountingObjectDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS, CHARGE_SERVICE_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accObject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Accounting object", ACCOBJECT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} должен вернуть 404 для несуществующего начисления")
    void shouldBeStatus404WhenUpdateForNonExistsCharge() throws Exception {
        var accObject = modelMapper.map(EntityForTest.createAccountingObject(null, new Service(UUID.randomUUID())),
                AccountingObjectDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(accObject)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Charge", CHARGE_SERVICE_ID_NON_EXISTS))));
    }




    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDelete() throws Exception {
        var before = chargeServiceRepository.findById(UUID.fromString(CHARGE_SERVICE_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS))
                .andExpect(status().isNoContent());

        var after = chargeServiceRepository.findById(UUID.fromString(CHARGE_SERVICE_ID_EXISTS));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} для несуществующего начисления должен вернуть статус 304")
    void shouldBeStatus304WhenDelNotExists() throws Exception {
        var before = chargeServiceRepository.findById(UUID.fromString(CHARGE_SERVICE_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_NON_EXISTS))
                .andExpect(status().isNotModified());
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} для несуществующего контрагента должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContragentNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} для несуществующего договора должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContractNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS, ACCOBJECT_ID_EXISTS, CHARGE_SERVICE_ID_EXISTS))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));

    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid} для несуществующего договора должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelAccObjectNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}",
                    CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS, ACCOBJECT_ID_NON_EXISTS, CHARGE_SERVICE_ID_EXISTS))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Accounting object", ACCOBJECT_ID_NON_EXISTS))));

    }

}
