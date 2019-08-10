package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.AccountingObject;
import ru.ptvi.otuscourse.clientroomapi.domain.ChargeService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChargeServiceRepository extends CrudRepository<ChargeService, UUID> {
    @Query("select c from ChargeService c where accObject in :accObjects and ((dateFrom between :date1 and :date2) or (dateTo between :date1 and :date2))")
    List<ChargeService> findByContractsBetweenDates(Collection<AccountingObject> accObjects, LocalDate date1, LocalDate date2);
}
