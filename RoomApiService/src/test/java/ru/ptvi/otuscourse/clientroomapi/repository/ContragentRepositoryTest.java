package ru.ptvi.otuscourse.clientroomapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContragentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContragentRepository contragentRepository;

    @Test
    public void mappingEntityWithPeopleTest() {
        var contragent = EntityForTest.createContragentAsPeople("vasia@pupkin.org");
        entityManager.persist(contragent);
        entityManager.flush();
        entityManager.detach(contragent);

        assertThat(contragent.getPeople().getId()).isNotNull();
        assertThat(contragent.getId()).isNotNull();

        Optional<Contragent> finded = contragentRepository.findById(contragent.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(contragent);
    }

    @Test
    public void mappingEntityWithFirmTest() {
        var contragent = EntityForTest.createContragentAsFirm("roga&kopyta@business.com");
        entityManager.persist(contragent);
        entityManager.flush();
        entityManager.detach(contragent);

        assertThat(contragent.getFirm().getId()).isNotNull();
        assertThat(contragent.getId()).isNotNull();

        Optional<Contragent> finded = contragentRepository.findById(contragent.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(contragent);
    }

    @Test
    public void whenFindByEmailFromDemoDb_thenAnyShouldBeFound() {
        String email = "petrov@mail.ru";

        Optional<Contragent> finded = contragentRepository.findByEmailOrPhone(email);

        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get().getEmail()).isEqualTo(email);

    }

}