package ru.ptvi.otuscourse.clientroomapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ptvi.otuscourse.clientroomapi.domain.DocuLink;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DocumentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocuLinkRepository documentRepository;

    @Test
    public void MappingEntityTest() {
        var contragent = EntityForTest.createContragentAsPeople("vasia@pupkin.org");
        var contract = EntityForTest.createContract(contragent, 0);
        var doculink = EntityForTest.createDocument(contract);

        entityManager.persist(contract.getContragent());
        entityManager.persist(contract);

        entityManager.persist(doculink);
        entityManager.flush();
        entityManager.detach(doculink);

        Optional<DocuLink> finded = documentRepository.findById(doculink.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(doculink);
    }
}