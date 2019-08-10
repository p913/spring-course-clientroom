package ru.ptvi.otuscourse.clientroomapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ptvi.otuscourse.clientroomapi.config.ModelMapperConfig;
import ru.ptvi.otuscourse.clientroomapi.domain.*;
import ru.ptvi.otuscourse.clientroomapi.repository.EntityForTest;
import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;
import ru.ptvi.otuscourse.clientroomdto.ContractDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentDto;
import ru.ptvi.otuscourse.clientroomdto.DemandDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ModelMapperConfig.class)
public class MappingDtoTests {
    @Autowired
    ModelMapper modelMapper;

    @Test
    public void shouldBeDtoSameAsContract() {
        var contract = EntityForTest.createContract(new Contragent(UUID.randomUUID()), -10.5);
        contract.setId(UUID.randomUUID());
        var dto = modelMapper.map(contract, ContractDto.class);

        assertThat(dto.number()).isEqualTo(contract.getNumber());
        assertThat(dto.balance()).isEqualTo(contract.getBalance());
        assertThat(dto.dateFrom()).isEqualTo(contract.getDateFrom());
        assertThat(dto.id()).isEqualTo(contract.getId().toString());
    }

    @Test
    public void shouldBeContractSameAsDto() {
        var id = UUID.randomUUID();
        var dto = new ContractDto()
                .id(id.toString())
                .number("111/222")
                .dateFrom(LocalDate.parse("2019-07-01"))
                .balance(BigDecimal.valueOf(-1000, 2));

        var contract = modelMapper.map(dto, Contract.class);

        assertThat(contract.getBalance()).isEqualTo(dto.balance());
        assertThat(contract.getDateFrom()).isEqualTo(dto.dateFrom());
        assertThat(contract.getAccObjects()).isNotNull().isEmpty();
        assertThat(contract.getId()).isNotNull().isEqualTo(id);
    }

    @Test
    public void shouldBeDtoSameAsDemandAndNestedEntityNotNull() {
        var contragent = EntityForTest.createContragentAsPeople("11@22.com");
        contragent.setId(UUID.randomUUID());

        var contract = EntityForTest.createContract(contragent, -10.5);
        contract.setId(UUID.randomUUID());

        var demand = EntityForTest.createDemand(contragent, contract, null);
        demand.setId(UUID.randomUUID());

        var dto = modelMapper.map(demand, DemandDto.class);

        assertThat(dto.demandSubject()).isEqualTo(demand.getDemandSubject().name());
        assertThat(dto.demandDateTime()).isEqualTo(demand.getDemandDateTime());
        assertThat(dto.accObject()).isNull();
        assertThat(dto.contract().id()).isEqualTo(contract.getId().toString());
        assertThat(dto.contract().balance()).isEqualTo(contract.getBalance());
        assertThat(dto.contract().number()).isEqualTo(contract.getNumber());
        assertThat(dto.id()).isEqualTo(demand.getId().toString());
    }

    @Test
    public void shouldBeDemandSameAsDtoAndAndNestedEntityNotNull() {
        var contractId = UUID.randomUUID();
        var accObjectId = UUID.randomUUID();

        var dto = new DemandDto()
                .contract(new ContractDto().id(contractId.toString()))
                .accObject(new AccountingObjectDto().id(accObjectId.toString()))
                .demandDateTime(OffsetDateTime.now())
                .demandSubject(DemandSubject.PAUSE.name());

        var demand = modelMapper.map(dto, Demand.class);

        assertThat(demand.getDemandDateTime()).isEqualTo(dto.demandDateTime());
        assertThat(demand.getDemandSubject().name()).isEqualTo(dto.demandSubject());
        assertThat(demand.getAccObject()).isNotNull().isEqualTo(new AccountingObject(accObjectId));
        assertThat(demand.getContragent()).isNull();
        assertThat(demand.getContract()).isNotNull().isEqualTo(new Contract(contractId));
    }

    @Test
    public void shouldBePeopleFilledWhenMapDtoToContract() {
        var peopleId = UUID.randomUUID();

        var people = new People(peopleId,
                "Peter", null,
                "Ivanov", null, LocalDate.parse("2000-01-31"));

        var dto = new ContragentDto()
                .peopleId(peopleId.toString())
                .peopleBirthday(people.getBirthday())
                .peopleFirstName(people.getFirstName())
                .peopleLastName(people.getLastName());

        var contragent = modelMapper.map(dto, Contragent.class);

        assertThat(contragent.getPeople())
                .isNotNull()
                .isEqualTo(people);
    }

    @Test
    public void shouldBeFirmFilledWhenMapDtoToContract() {
        var firmId = UUID.randomUUID();

        var firm = new Firm(firmId,
                "OOO Solnyshko",
                "6159199191",
                "6150000009",
                "Sberbank PAO",
                "40000230064519005937");

        var dto = new ContragentDto()
                .firmId(firmId.toString())
                .firmAccount(firm.getAccount())
                .firmBank(firm.getBank())
                .firmInn(firm.getInn())
                .firmKpp(firm.getKpp())
                .firmName(firm.getName());

        var contragent = modelMapper.map(dto, Contragent.class);

        assertThat(contragent.getFirm())
                .isNotNull()
                .isEqualTo(firm);
    }

    @Test
    public void shouldBePeopleFilledWhenMapContractToDto() {
        var contragent = EntityForTest.createContragentAsPeople("11@22.com");
        contragent.getPeople().setId(UUID.randomUUID());

        var dto = modelMapper.map(contragent, ContragentDto.class);

        assertThat(dto.peopleFirstName()).isEqualTo(contragent.getPeople().getFirstName());
        assertThat(dto.peopleMiddleName()).isEqualTo(contragent.getPeople().getMiddleName());
        assertThat(dto.peopleLastName()).isEqualTo(contragent.getPeople().getLastName());
        assertThat(dto.peopleBirthday()).isEqualTo(contragent.getPeople().getBirthday());
        assertThat(dto.peoplePassport()).isEqualTo(contragent.getPeople().getPassport());
        assertThat(dto.peopleId()).isEqualTo(contragent.getPeople().getId().toString());
    }
}
