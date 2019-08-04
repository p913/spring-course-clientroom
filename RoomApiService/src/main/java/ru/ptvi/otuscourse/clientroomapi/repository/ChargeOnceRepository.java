package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.ChargeOnce;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChargeOnceRepository extends CrudRepository<ChargeOnce, UUID> {
    @Query("select c from ChargeOnce c where contract in :contracts and ((dateFrom between :date1 and :date2) or (dateTo between :date1 and :date2))")
    List<ChargeOnce> findByContractsBetweenDates(Collection<Contract> contracts, LocalDate date1, LocalDate date2);
}
