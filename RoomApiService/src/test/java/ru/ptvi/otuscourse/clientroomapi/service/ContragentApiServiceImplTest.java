package ru.ptvi.otuscourse.clientroomapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ptvi.otuscourse.clientroomapi.config.ModelMapperConfig;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;
import ru.ptvi.otuscourse.clientroomapi.domain.People;
import ru.ptvi.otuscourse.clientroomapi.repository.AccountingObjectRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.ContractRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.ContragentRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomdto.ContragentDto;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContragentApiServiceImplTest {

    @Configuration
    public static class Cfg {
        @MockBean
        private ContragentRepository contragentRepository;

        @MockBean
        private ContractRepository contractRepository;

        @MockBean
        private AccountingObjectRepository accountingObjectRepository;

        @MockBean
        private ModelMapper modelMapper;

        @Bean
        public ContragentApiService contragentApiService() {
            return new ContragentApiServiceImpl(contragentRepository, contractRepository, accountingObjectRepository, modelMapper);
        }

    }

    @Autowired
    private ContragentRepository contragentRepository;

    @Autowired
    private ContragentApiService contragentApiService;

    @Test
    void shouldThrowWhenPatchPeopleToFirm() {
        var contragent = EntityForTest.createContragentAsPeople("e@mail.ru");
        contragent.setId(UUID.randomUUID());

        when(contragentRepository.findById(any())).thenReturn(Optional.of(contragent));

        var contragentDto = new ContragentDto().id(contragent.getId().toString());
        contragentDto.firmName("New firm");

        assertThatThrownBy(() -> contragentApiService.patchContagent(contragentDto)).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    void shouldThrowWhenPatchFirmToPeople() {
        var contragent = EntityForTest.createContragentAsFirm("e@mail.ru");
        contragent.setId(UUID.randomUUID());

        when(contragentRepository.findById(any())).thenReturn(Optional.of(contragent));

        var contragentDto = new ContragentDto().id(contragent.getId().toString());
        contragentDto.peopleFirstName("First name").peopleLastName("Last name");

        assertThatThrownBy(() -> contragentApiService.patchContagent(contragentDto)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldSaveEntityWithNewFields() {
        var contragent = EntityForTest.createContragentAsPeople("e@mail.ru");
        contragent.setId(UUID.randomUUID());

        var contragentDto = new ContragentDto().id(contragent.getId().toString());
        contragentDto.peopleFirstName("First name").peopleLastName("Last name");
        contragentDto.address("New address");

        var contragent2 = new Contragent(contragent.getId(),
                contragent.getEmail(),
                new People(contragent.getPeople().getFirstName(),
                        contragent.getPeople().getMiddleName(),
                        contragent.getPeople().getLastName(),
                        contragent.getPeople().getPassport(),
                        contragent.getPeople().getBirthday()),
                null,
                contragent.getAddress(),
                contragent.getPhone(),
                contragent.getPassword());

        contragent2.setAddress("New address");
        contragent2.getPeople().setFirstName("First name");
        contragent2.getPeople().setLastName("Last name");

        when(contragentRepository.findById(any())).thenReturn(Optional.of(contragent));
        contragentApiService.patchContagent(contragentDto);

        verify(contragentRepository).save(contragent2);
    }


}