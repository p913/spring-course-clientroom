package ru.ptvi.otuscourse.clientroomapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ptvi.otuscourse.clientroomapi.domain.Demand;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DemandRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DemandRepository demandRepository;

    @Test
    public void MappingEntityTest() {
        var contragent = EntityForTest.createContragentAsPeople("vasia@pupkin.org");
        var contract = EntityForTest.createContract(contragent, 0);
        var service = EntityForTest.createService();
        var accObject = EntityForTest.createAccountingObject(contract, service);
        var demand = EntityForTest.createDemand(contragent, contract, accObject);

        entityManager.persist(contract.getContragent());
        entityManager.persist(contract);
        entityManager.persist(service);
        entityManager.persist(accObject);

        entityManager.persist(demand);
        entityManager.flush();
        entityManager.detach(demand);

        Optional<Demand> finded = demandRepository.findById(demand.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(demand);
    }
}