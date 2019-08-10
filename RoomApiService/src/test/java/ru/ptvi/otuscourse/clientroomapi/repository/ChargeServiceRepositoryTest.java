package ru.ptvi.otuscourse.clientroomapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ptvi.otuscourse.clientroomapi.domain.AccountingObject;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChargeServiceRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountingObjectRepository accObjectRepository;

    @Test
    public void MappingEntityTest() {
        var contragent = EntityForTest.createContragentAsPeople("vasia@pupkin.org");
        var contract = EntityForTest.createContract(contragent, 0);
        var service = EntityForTest.createService();
        var accObject = EntityForTest.createAccountingObject(contract, service);
        var charge = EntityForTest.createChargeService(accObject, service);

        entityManager.persist(contract.getContragent());
        entityManager.persist(contract);
        entityManager.persist(service);
        entityManager.persist(accObject);

        entityManager.persist(charge);
        entityManager.flush();
        entityManager.detach(charge);

        Optional<AccountingObject> finded = accObjectRepository.findById(accObject.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(accObject);
    }
}