package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;
import ru.ptvi.otuscourse.clientroomapi.domain.Pay;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PayRepository extends CrudRepository<Pay, UUID> {
    @Query("select p from Pay p where contract in :contracts and payDateTime between :date1 and :date2")
    List<Pay> findByContractsBetweenDates(Collection<Contract> contracts, OffsetDateTime date1, OffsetDateTime date2);
}
