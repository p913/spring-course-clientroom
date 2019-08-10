package ru.ptvi.otuscourse.clientroomapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ptvi.otuscourse.clientroomapi.domain.Service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    public void mappingEntityTest() {
        var service = EntityForTest.createService();

        entityManager.persist(service);
        entityManager.flush();
        entityManager.detach(service);

        assertThat(service.getId()).isNotNull();

        Optional<Service> finded = serviceRepository.findById(service.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(service);
    }

}
