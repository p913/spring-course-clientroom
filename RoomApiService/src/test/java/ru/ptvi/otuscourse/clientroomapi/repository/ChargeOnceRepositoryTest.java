package ru.ptvi.otuscourse.clientroomapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ptvi.otuscourse.clientroomapi.domain.ChargeOnce;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChargeOnceRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChargeOnceRepository chargeOnceRepository;

    @Test
    public void MappingEntityTest() {
        var contragent = EntityForTest.createContragentAsPeople("vasia@pupkin.org");
        var contract = EntityForTest.createContract(contragent, 0);
        var charge = EntityForTest.createChargeOnce(contract);

        entityManager.persist(contract.getContragent());
        entityManager.persist(contract);

        entityManager.persist(charge);
        entityManager.flush();
        entityManager.detach(charge);

        Optional<ChargeOnce> finded = chargeOnceRepository.findById(charge.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(charge);
    }
}