package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.Service;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends CrudRepository<Service, UUID> {
    @Override
    List<Service> findAll();
}
