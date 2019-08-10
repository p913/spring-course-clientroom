package ru.ptvi.otuscourse.clientroomapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;
import ru.ptvi.otuscourse.clientroomapi.domain.Notification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, UUID> {
    @Query("select n from Notification n where contragent = :contragent and viewed = false order by n.originDateTime desc")
    List<Notification> findByContragentUnviewedOnly(Contragent contragent);

    @Query("select n from Notification n where contragent = :contragent and originDateTime between :date1 and :date2 order by n.originDateTime desc")
    List<Notification> findByContragentBetweenDates(Contragent contragent, OffsetDateTime date1, OffsetDateTime date2);
}
