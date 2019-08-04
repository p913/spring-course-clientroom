package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;
import ru.ptvi.otuscourse.clientroomapi.domain.Demand;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DemandRepository extends CrudRepository<Demand, UUID> {
    @Query("select d from Demand d where contragent = :contragent and demandDateTime between :date1 and :date2 order by d.demandDateTime desc")
    List<Demand> findByContragentBetweenDates(Contragent contragent, OffsetDateTime date1, OffsetDateTime date2);
}

