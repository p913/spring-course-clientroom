package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.AccountingObject;

import java.util.UUID;

@Repository
public interface AccountingObjectRepository extends CrudRepository<AccountingObject, UUID> {
}
