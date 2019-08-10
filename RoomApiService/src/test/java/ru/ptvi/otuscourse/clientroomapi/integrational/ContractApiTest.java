package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.ptvi.otuscourse.clientroomapi.repository.ContractRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomdto.ContractDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ContractApiTest extends BaseApiTest {

    @Autowired
    private ContractRepository contractRepository;

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts должен возвращать массив объектов, содержащих основные атрибуты договора")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts", CONTRAGENT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].number").isNotEmpty())
                .andExpect(jsonPath("$[0].dateFrom").isNotEmpty())
                .andExpect(jsonPath("$[0].balance").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts должен возвращать пустой массив для несуществующего контрагента")
    void shouldBeOkAndEmptyArrayWhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts", CONTRAGENT_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid} должен возвращать объект, содержащий основные атрибуты договора")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_EXISTS , CONTRACT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.number").isNotEmpty())
                .andExpect(jsonPath("$.dateFrom").isNotEmpty())
                .andExpect(jsonPath("$.balance").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid} должен возвращать 404 для несуществующего контрагента")
    void shouldBeStatus404WhenContragentNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid}/contracts/{ctid} должен возвращать 404 для несуществующего договора")
    void shouldBeStatus404WhenContractNotExists() throws Exception {
        mvc.perform(get("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBeStatus201AndReturnLocationAndId() throws Exception {
        var contract = modelMapper.map(EntityForTest.createContract(null, 0), ContractDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts", CONTRAGENT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(contract)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/contragents/" + CONTRAGENT_ID_EXISTS + "/contracts/")))
                .andReturn();

        assertThat(contractRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();
    }

    @Test
    @DisplayName("POST /api/contragents/{cgid}/contracts должен вернуть 404 для несуществующего контрагента")
    void shouldBeStatus404WhenCreateForNonExistsContragent() throws Exception {
        var contract = modelMapper.map(EntityForTest.createContract(null, 0), ContractDto.class);

        var res = mvc.perform(post("/api/contragents/{cgid}/contracts", CONTRAGENT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(contract)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }


    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid} должен обновить баланс")
    void shouldBeChangeBalanceWhenUpdate() throws Exception {
        var balance = BigDecimal.valueOf(-1020.50);
        var contract = contractRepository.findById(UUID.fromString(CONTRACT_ID_EXISTS));

        assertThat(contract.isPresent()).isTrue();

        contract.get().setBalance(balance);

        var dto = modelMapper.map(contract.get(), ContractDto.class);

        mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = contractRepository.findById(UUID.fromString(CONTRACT_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getBalance()).isEqualTo(balance);
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid} должен вернуть 404 для несуществующего контрагента но существующего договора")
    void shouldBeStatus404WhenUpdateForNonExistsContragent() throws Exception {
        var contract = modelMapper.map(EntityForTest.createContract(null, 0), ContractDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(contract)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid}/contracts/{ctid} должен вернуть 404 для существующего контрагента но несуществующего договора")
    void shouldBeStatus404WhenUpdateForNonExistsContract() throws Exception {
        var contract = modelMapper.map(EntityForTest.createContract(null, 0), ContractDto.class);

        var res = mvc.perform(put("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(contract)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                        stringContainsInOrder(Arrays.asList("Contract", CONTRACT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDeleteWhenDel() throws Exception {
        var before = contractRepository.findById(UUID.fromString(CONTRACT_ID_WITHOUT_REFERENCES));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_EXISTS, CONTRACT_ID_WITHOUT_REFERENCES))
                .andExpect(status().isNoContent());

        var after = contractRepository.findById(UUID.fromString(CONTRACT_ID_WITHOUT_REFERENCES));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid} для несуществующего должен вернуть статус 304")
    void shouldBeStatus304WhenDelContractNotExists() throws Exception {
        var before = contractRepository.findById(UUID.fromString(CONTRACT_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_EXISTS, CONTRACT_ID_NON_EXISTS))
                .andExpect(status().isNotModified());

    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid} для несуществующего контрагента должен вернуть статус 404 и описание ошибки")
    void shouldBeStatus404WhenDelContragentNotExists() throws Exception {
        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_NON_EXISTS, CONTRACT_ID_WITHOUT_REFERENCES))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Error-Message",
                    stringContainsInOrder(Arrays.asList("Contragent", CONTRAGENT_ID_NON_EXISTS))));
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid}/contracts/{ctid} для имеющего в зависимостях объекты учета должен вернуть статус 409")
    void shouldBeStatus409WhenHasReferences() throws Exception {
        var before = contractRepository.findById(UUID.fromString(CONTRACT_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}/contracts/{ctid}", CONTRAGENT_ID_EXISTS, CONTRACT_ID_EXISTS))
                .andExpect(status().isConflict());
    }


}
