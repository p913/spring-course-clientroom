package ru.ptvi.otuscourse.clientroomapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;
import ru.ptvi.otuscourse.clientroomapi.domain.Pay;
import ru.ptvi.otuscourse.clientroomapi.repository.ContractRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.PayRepository;
import ru.ptvi.otuscourse.clientroomapi.utils.DateDiapUtils;
import ru.ptvi.otuscourse.clientroomdto.PayDto;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PayApiServiceImpl implements PayApiService {
    private final PayRepository payRepository;

    private final ContractRepository contractRepository;

    private final ModelMapper modelMapper;

    public PayApiServiceImpl(PayRepository payRepository, ContractRepository contractRepository, ModelMapper modelMapper) {
        this.payRepository = payRepository;
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PayDto> getBetweenDates(String contragentId, String contractId, LocalDate from, LocalDate to) {
        return payRepository
                .findByContractsBetweenDates(Collections.singletonList(new Contract(UUID.fromString(contractId))),
                        DateDiapUtils.dateTimeStartOfDay(from),
                        DateDiapUtils.dateTimeEndOfDay(to))
                .stream()
                .filter(p -> p.getContract().getContragent().getId().toString().equals(contragentId))
                .map(p -> modelMapper.map(p, PayDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PayDto> getById(String contragentId, String contractId, String payId) {
        return payRepository
                .findById(UUID.fromString(payId))
                .filter(p -> p.getContract().getId().toString().equals(contractId))
                .filter(p -> p.getContract().getContragent().getId().toString().equals(contragentId))
                .map(p -> modelMapper.map(p, PayDto.class));
    }

    @Override
    public void create(String contragentId, String contractId, PayDto payDto) {
        var contract = contractRepository.findById(UUID.fromString(contractId));
        if (contract.isEmpty())
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!contract.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var pay = modelMapper.map(payDto, Pay.class);
        pay.setContract(contract.get());
        pay.setId(null);
        payRepository.save(pay);
        payDto.id(pay.getId().toString());
    }

    @Override
    public void update(String contragentId, String contractId, PayDto payDto) {
        var doc = payRepository.findById(UUID.fromString(payDto.id()));

        if (doc.isEmpty())
            throw new EntityNotFoundException("Pay not found: " + payDto.id());

        if (!doc.get().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!doc.get().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var payUpdate = modelMapper.map(payDto, Pay.class);
        payUpdate.setContract(doc.get().getContract());
        payRepository.save(payUpdate);
    }

    @Override
    public boolean delete(String contragentId, String contractId, String payId) {
        UUID id = UUID.fromString(payId);
        var doc = payRepository.findById(id);

        if (doc.isEmpty())
            return false;

        if (!doc.get().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!doc.get().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        payRepository.deleteById(id);
        return true;
    }
}
