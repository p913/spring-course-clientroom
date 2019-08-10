package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContragentRepository extends CrudRepository<Contragent, UUID> {
    @Override
    List<Contragent> findAll();

    @Query("select c from Contragent c where email = :param or phone = :param")
    Optional<Contragent> findByEmailOrPhone(String param);
}
