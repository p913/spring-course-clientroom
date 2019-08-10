package ru.ptvi.otuscourse.clientroomapi.integrational;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomapi.repository.ServiceRepository;
import ru.ptvi.otuscourse.clientroomdto.ServiceDto;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ServiceApiTest extends BaseApiTest {
    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    @DisplayName("GET /api/services должен возвращать массив объектов, содержащих основные атрибуты услуг")
    void shouldBeOkAndReturnArrayOfPredefined() throws Exception {
        mvc.perform(get("/api/services")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].cost").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/services/{sid} должен возвращать объект, содержащий основные атрибуты услуги")
    void shouldBeOkAndReturnPredefined () throws Exception {
        mvc.perform(get("/api/services/" + SERVICE_ID_EXISTS)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SERVICE_ID_EXISTS))
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.cost").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/services/{sid} должен возвращать статус 404 для несуществующей услуги")
    void shouldBeNotFound() throws Exception {
        mvc.perform(get("/api/services/" + SERVICE_ID_NON_EXISTS))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/services/ должен создать объект в БД, вернуть статус 201, id и location нового объекта")
    void shouldBe201AndReturnLocationAndId() throws Exception {
        var service = modelMapper.map(EntityForTest.createService(), ServiceDto.class);

        var res = mvc.perform(post("/api/services/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(service)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/services/")))
                .andReturn();

        assertThat(serviceRepository.findById(UUID.fromString(res.getResponse().getContentAsString())).isPresent()).isTrue();

    }

    @Test
    @DisplayName("PUT /api/services/{sid} должен обновить cost")
    void shouldBeChangePhoneAndPeopleWhenUpdate() throws Exception {
        var cost = BigDecimal.valueOf(10.56);
        var service = serviceRepository.findById(UUID.fromString(SERVICE_ID_EXISTS));

        assertThat(service.isPresent()).isTrue();

        service.get().setCost(cost);

        var dto = modelMapper.map(service.get(), ServiceDto.class);

        mvc.perform(put("/api/services/{sid}", SERVICE_ID_EXISTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var updated = serviceRepository.findById(UUID.fromString(SERVICE_ID_EXISTS));

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getCost()).isEqualTo(cost);
    }

    @Test
    @DisplayName("DELETE /api/services/{sid} для существующего должен вернуть статус 204 и физически удалить")
    void shouldBeStatus204AndNotExistsAfterDelete() throws Exception {
        var before = serviceRepository.findById(UUID.fromString(SERVICE_ID_WITHOUT_REFERENCES));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/services/{sid}", SERVICE_ID_WITHOUT_REFERENCES))
                .andExpect(status().isNoContent());

        var after = serviceRepository.findById(UUID.fromString(SERVICE_ID_WITHOUT_REFERENCES));
        assertThat(after.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE /api/services/{sid} для несуществующего должен вернуть статус 304")
    void shouldBeStatus304WhenNotExists() throws Exception {
        var before = serviceRepository.findById(UUID.fromString(SERVICE_ID_NON_EXISTS));
        assertThat(before.isPresent()).isFalse();

        mvc.perform(delete("/api/services/{sid}", SERVICE_ID_NON_EXISTS))
                .andExpect(status().isNotModified());

    }

    @Test
    @DisplayName("DELETE /api/services/{sid} для услуги с зависимостями (объекты учета. начисления) должен вернуть статус 409")
    void shouldBeStatus409WhenHasReferences() throws Exception {
        var before = serviceRepository.findById(UUID.fromString(SERVICE_ID_EXISTS));
        assertThat(before.isPresent()).isTrue();

        mvc.perform(delete("/api/services/{sid}", SERVICE_ID_EXISTS))
                .andExpect(status().isConflict());

    }

}
