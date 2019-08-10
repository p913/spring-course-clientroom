package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;
import ru.ptvi.otuscourse.clientroomapi.domain.DocuLink;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocuLinkRepository extends CrudRepository<DocuLink, UUID> {
    @Query("select d from DocuLink d where contract in :contracts and date between :date1 and :date2")
    List<DocuLink> findByContractsBetweenDates(Collection<Contract> contracts, LocalDate date1, LocalDate date2);

    @Query("select d from DocuLink d where contract in :contracts and date between :date1 and :date2")
    List<DocuLink> findByContragentBetweenDates(Collection<Contract> contracts, LocalDate date1, LocalDate date2);

}
