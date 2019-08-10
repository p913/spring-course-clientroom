package ru.ptvi.otuscourse.clientroomapi.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;
import ru.ptvi.otuscourse.clientroomapi.domain.Notification;
import ru.ptvi.otuscourse.clientroomapi.notify.NotifyGateway;
import ru.ptvi.otuscourse.clientroomapi.notify.NotifyMailService;
import ru.ptvi.otuscourse.clientroomapi.notify.NotifyMessagePayload;
import ru.ptvi.otuscourse.clientroomapi.notify.NotifySmsService;
import ru.ptvi.otuscourse.clientroomapi.repository.ContragentRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.NotificationRepository;
import ru.ptvi.otuscourse.clientroomapi.utils.DateDiapUtils;
import ru.ptvi.otuscourse.clientroomdto.NotificationDto;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationApiServiceImpl implements NotificationApiService {
    private final NotificationRepository notificationRepository;

    private final ContragentRepository contragentRepository;

    private final ModelMapper modelMapper;

    private final NotifyGateway notifyGateway;

    public NotificationApiServiceImpl(NotificationRepository notificationRepository, ContragentRepository contragentRepository,
                                      ModelMapper modelMapper, NotifyGateway notifyGateway) {
        this.notificationRepository = notificationRepository;
        this.contragentRepository = contragentRepository;
        this.modelMapper = modelMapper;
        this.notifyGateway = notifyGateway;
    }

    @Override
    public List<NotificationDto> getBetweenDates(String contragentId, LocalDate from, LocalDate to) {
        return notificationRepository
                .findByContragentBetweenDates(new Contragent(UUID.fromString(contragentId)),
                        DateDiapUtils.dateTimeStartOfDay(from),
                        DateDiapUtils.dateTimeEndOfDay(to))
                .stream()
                .map(n -> modelMapper.map(n, NotificationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> getUnviewed(String contragentId) {
        return notificationRepository
                .findByContragentUnviewedOnly(new Contragent(UUID.fromString(contragentId)))
                .stream()
                .map(n -> modelMapper.map(n, NotificationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationDto> getById(String contragentId, String notId) {
        return notificationRepository
                .findById(UUID.fromString(notId))
                .filter(n -> n.getContragent().getId().toString().equals(contragentId))
                .map(n -> modelMapper.map(n, NotificationDto.class));
    }

    @Override
    public void create(String contragentId, NotificationDto notDto) {
        var contragent = contragentRepository.findById(UUID.fromString(contragentId));
        if (contragent.isEmpty())
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var notification = modelMapper.map(notDto, Notification.class);
        notification.setContragent(contragent.get());
        notification.setId(null);
        notificationRepository.save(notification);
        notDto.id(notification.getId().toString());

        notifyGateway.send(new NotifyMessagePayload()
                .email(contragent.get().getEmail())
                .phone(contragent.get().getPhone())
                .message(notDto.message())
                .sendEmail(notDto.sendEmail())
                .sendSms(notDto.sendSms()));
    }

    @Override
    public void update(String contragentId, NotificationDto notDto) {
        var notification = notificationRepository.findById(UUID.fromString(notDto.id()));

        if (notification.isEmpty())
            throw new EntityNotFoundException("Notification not found: " + notDto.id());

        if (!notification.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var notifUpdate = modelMapper.map(notDto, Notification.class);
        notifUpdate.setContragent(notification.get().getContragent());
        notificationRepository.save(notifUpdate);
    }

    @Override
    public boolean delete(String contragentId, String notId) {
        UUID id = UUID.fromString(notId);
        var doc = notificationRepository.findById(id);

        if (doc.isEmpty())
            return false;

        if (!doc.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        notificationRepository.deleteById(id);
        return true;
    }
}
