package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.ptvi.otuscourse.clientroomapi.repository.ContragentRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomdto.ContragentDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContragentApiTest extends BaseApiTest {

    @Autowired
    private ContragentRepository contragentRepository;

    @Test
    @DisplayName("GET /api/contragents должtн возвращать массив объектов, содержащих основные атрибуты контрагента")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/contragents")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].firmId").hasJsonPath())
                .andExpect(jsonPath("$[0].peopleId").hasJsonPath())
                .andExpect(jsonPath("$[0].email").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid} должен возвращать объект, содержащий основные атрибуты контрагента")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/contragents/" + CONTRAGENT_ID_EXISTS)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CONTRAGENT_ID_EXISTS))
                .andExpect(jsonPath("$.firmId").hasJsonPath())
                .andExpect(jsonPath("$.peopleId").hasJsonPath())
                .andExpect(jsonPath("$.email").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/contragents/{cgid} должен возвращать статус 404 для несуществующего контрагента")
    void shouldBeNotFound() throws Exception {
        mvc.perform(get("/api/contragents/" + CONTRAGENT_ID_NON_EXISTS))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/contragents/ должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBe201AndReturnLocationAndId() throws Exception {
        var contragent = modelMapper.map(EntityForTest.createContragentAsPeople("pupkin@mail.ru"), ContragentDto.class);

        var res = mvc.perform(post("/api/contragents/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(contragent)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/contragents/")))
                .andReturn();

        assertThat(contragentRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();

    }

    @Test
    @DisplayName("PUT /api/contragents/{cgid} должен обновить phone и заменить firm на people")
    void shouldBeChangePhoneAndPeopleWhenUpdate() throws Exception {
        var contragent = contragentRepository.findById(UUID.fromString(CONTRAGENT_ID_EXISTS));

        assertThat(contragent.isPresent()).isTrue();
        assertThat(contragent.get().getPeople()).isNotNull();

        var firm = EntityForTest.createFirm();

        contragent.get().setPhone("+79097778899");
        contragent.get().setFirm(firm);
        contragent.get().setPeople(null);

        var dto = modelMapper.map(contragent.get(), ContragentDto.class);

        mvc.perform(put("/api/contragents/{cgid}", CONTRAGENT_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = contragentRepository.findById(UUID.fromString(CONTRAGENT_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getPeople()).isNull();
        assertThat(updated.get().getFirm()).isNotNull();

        firm.setId(updated.get().getFirm().getId());
        assertThat(updated.get().getFirm()).isEqualTo(firm);
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDelete() throws Exception {
        var before = contragentRepository.findById(UUID.fromString(CONTRAGENT_ID_WITHOUT_REFERECNES));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/contragents/{cgid}", CONTRAGENT_ID_WITHOUT_REFERECNES))
                .andExpect(status().isNoContent());

        var after = contragentRepository.findById(UUID.fromString(CONTRAGENT_ID_WITHOUT_REFERECNES));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid} для несуществующего должен вернуть статус 304")
    void shouldBeStatus304WhenNotExists() throws Exception {
        var before = contragentRepository.findById(UUID.fromString(CONTRAGENT_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/contragents/{cgid}", CONTRAGENT_ID_NON_EXISTS))
                .andExpect(status().isNotModified());

    }

    @Test
    @DisplayName("DELETE /api/contragents/{cgid} для имеющего в зависимостях договора должен вернуть статус 409")
    void shouldBeStatus409WhenHasReferences() throws Exception {
        var before = contragentRepository.findById(UUID.fromString(CONTRAGENT_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();
        assertThat(before.get().getContracts()).isNotEmpty();

        mvc.perform(delete("/api/contragents/" + CONTRAGENT_ID_EXISTS))
                .andExpect(status().isConflict());
    }

}