package ru.ptvi.otuscourse.clientroomapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContractRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    public void mappingEntityTest() {
        var contragent = EntityForTest.createContragentAsPeople("vasia@pupkin.org");
        var contract = EntityForTest.createContract(contragent, 0);

        entityManager.persist(contract.getContragent());

        entityManager.persist(contract);
        entityManager.flush();
        entityManager.detach(contract);

        Optional<Contract> finded = contractRepository.findById(contract.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(contract);
    }
}
