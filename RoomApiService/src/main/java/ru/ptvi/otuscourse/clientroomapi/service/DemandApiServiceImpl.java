package ru.ptvi.otuscourse.clientroomapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;
import ru.ptvi.otuscourse.clientroomapi.domain.Demand;
import ru.ptvi.otuscourse.clientroomapi.repository.ContragentRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.DemandRepository;
import ru.ptvi.otuscourse.clientroomapi.utils.DateDiapUtils;
import ru.ptvi.otuscourse.clientroomdto.DemandDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DemandApiServiceImpl implements DemandApiService {
    private final DemandRepository demandRepository;

    private final ContragentRepository contragentRepository;

    private final ModelMapper modelMapper;

    public DemandApiServiceImpl(DemandRepository demandRepository, ContragentRepository contragentRepository, ModelMapper modelMapper) {
        this.demandRepository = demandRepository;
        this.contragentRepository = contragentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DemandDto> getBetweenDates(String contragentId, LocalDate from, LocalDate to) {
        return demandRepository
                .findByContragentBetweenDates(new Contragent(UUID.fromString(contragentId)),
                        DateDiapUtils.dateTimeStartOfDay(from),
                        DateDiapUtils.dateTimeEndOfDay(to))
                .stream()
                .filter(d -> d.getContragent().getId().toString().equals(contragentId))
                .peek(d -> { if (d.getContract() != null) d.getContract().setAccObjects(Collections.EMPTY_LIST); })
                .map(d -> modelMapper.map(d, DemandDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DemandDto> getById(String contragentId, String demandId) {
        return demandRepository
                .findById(UUID.fromString(demandId))
                .filter(n -> n.getContragent().getId().toString().equals(contragentId))
                .map(d -> { if (d.getContract() != null) d.getContract().setAccObjects(Collections.EMPTY_LIST); return d; })
                .map(n -> modelMapper.map(n, DemandDto.class));
    }

    @Override
    public void create(String contragentId, DemandDto demandDto) {
        var contragent = contragentRepository.findById(UUID.fromString(contragentId));
        if (contragent.isEmpty())
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        if (demandDto.contract() != null
                && !StringUtils.isEmpty(demandDto.contract().id())
                && !contragent.get().getContracts().stream()
                        .anyMatch(c -> c.getId().toString().equals(demandDto.contract().id())))
            throw new EntityNotFoundException("Contract not found: " + demandDto.contract().id());

        if (demandDto.accObject() != null
                && !StringUtils.isEmpty(demandDto.accObject().id())
                && !contragent.get().getContracts().stream().flatMap(c -> c.getAccObjects().stream())
                    .anyMatch(a -> a.getId().toString().equals(demandDto.accObject().id())))
            throw new EntityNotFoundException("Accounting object not found: " + demandDto.accObject().id());

        var demand = modelMapper.map(demandDto, Demand.class);
        demand.setContragent(contragent.get());
        demand.setId(null);
        demandRepository.save(demand);
        demandDto.id(demand.getId().toString());
    }

    @Override
    public void update(String contragentId, DemandDto demandDto) {
        var notification = demandRepository.findById(UUID.fromString(demandDto.id()));

        if (notification.isEmpty())
            throw new EntityNotFoundException("Demand not found: " + demandDto.id());

        if (!notification.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var demandUpdate = modelMapper.map(demandDto, Demand.class);
        demandUpdate.setContragent(notification.get().getContragent());
        demandRepository.save(demandUpdate);
    }

    @Override
    public boolean delete(String contragentId, String demandId) {
        UUID id = UUID.fromString(demandId);
        var doc = demandRepository.findById(id);

        if (doc.isEmpty())
            return false;

        if (!doc.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        demandRepository.deleteById(id);
        return true;
    }
}
