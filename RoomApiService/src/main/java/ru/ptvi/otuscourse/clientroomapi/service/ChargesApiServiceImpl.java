package ru.ptvi.otuscourse.clientroomapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ptvi.otuscourse.clientroomapi.domain.ChargeOnce;
import ru.ptvi.otuscourse.clientroomapi.domain.ChargeService;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;
import ru.ptvi.otuscourse.clientroomapi.repository.ChargeOnceRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.ChargeServiceRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.ContractRepository;
import ru.ptvi.otuscourse.clientroomdto.ChargeOnceDto;
import ru.ptvi.otuscourse.clientroomdto.ChargeServiceDto;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChargesApiServiceImpl implements ChargesApiService {
    private final ContractRepository contractRepository;

    private final ChargeOnceRepository chargeOnceRepository;

    private final ChargeServiceRepository chargeServiceRepository;

    private final ModelMapper modelMapper;

    public ChargesApiServiceImpl(ContractRepository contractRepository, ChargeOnceRepository chargeOnceRepository,
                                 ChargeServiceRepository chargeServiceRepository, ModelMapper modelMapper) {
        this.contractRepository = contractRepository;
        this.chargeOnceRepository = chargeOnceRepository;
        this.chargeServiceRepository = chargeServiceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ChargeOnceDto> getChargeOnceBetweenDates(String contragentId, String contractId, LocalDate from, LocalDate to) {
        return chargeOnceRepository
                .findByContractsBetweenDates(Collections.singletonList(new Contract(UUID.fromString(contractId))),
                        from, to)
                .stream()
                .filter(p -> p.getContract().getContragent().getId().toString().equals(contragentId))
                .map(p -> modelMapper.map(p, ChargeOnceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ChargeOnceDto> getChargeOnceById(String contragentId, String contractId, String chargeId) {
        return chargeOnceRepository
                .findById(UUID.fromString(chargeId))
                .filter(p -> p.getContract().getId().toString().equals(contractId))
                .filter(p -> p.getContract().getContragent().getId().toString().equals(contragentId))
                .map(p -> modelMapper.map(p, ChargeOnceDto.class));
    }

    @Override
    public void createChargeOnce(String contragentId, String contractId, ChargeOnceDto chargeDto) {
        var contract = contractRepository.findById(UUID.fromString(contractId));
        if (contract.isEmpty())
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!contract.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var chargeOnce = modelMapper.map(chargeDto, ChargeOnce.class);
        chargeOnce.setContract(contract.get());
        chargeOnce.setId(null);
        chargeOnceRepository.save(chargeOnce);
        chargeDto.id(chargeOnce.getId().toString());
    }

    @Override
    public void updateChargeOnce(String contragentId, String contractId, ChargeOnceDto chargeDto) {
        var charge = chargeOnceRepository.findById(UUID.fromString(chargeDto.id()));

        if (charge.isEmpty())
            throw new EntityNotFoundException("Charge not found: " + chargeDto.id());

        if (!charge.get().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!charge.get().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var chargeOnce = modelMapper.map(chargeDto, ChargeOnce.class);
        chargeOnce.setContract(charge.get().getContract());
        chargeOnceRepository.save(chargeOnce);

    }

    @Override
    public boolean deleteChargeOnce(String contragentId, String contractId, String chargeId) {
        UUID id = UUID.fromString(chargeId);
        var charge = chargeOnceRepository.findById(id);

        if (charge.isEmpty())
            return false;

        if (!charge.get().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!charge.get().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        chargeOnceRepository.deleteById(id);
        return true;
    }




    @Override
    public List<ChargeServiceDto> getChargeServiceBetweenDates(String contragentId, String contractId, String accObjectId, LocalDate from, LocalDate to) {
        var contract = contractRepository.findById(UUID.fromString(contractId));

        return chargeServiceRepository
                .findByContractsBetweenDates(contractRepository.findById(UUID.fromString(contractId)).orElse(new Contract()).getAccObjects(),
                        from, to)
                .stream()
                .filter(c -> c.getAccObject().getContract().getContragent().getId().toString().equals(contragentId))
                .filter(c -> c.getAccObject().getId().toString().equals(accObjectId))
                .map(p -> modelMapper.map(p, ChargeServiceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ChargeServiceDto> getChargeServiceById(String contragentId, String contractId, String accObjectId, String chargeId) {
        return chargeServiceRepository
                .findById(UUID.fromString(chargeId))
                .filter(c -> c.getAccObject().getContract().getContragent().getId().toString().equals(contragentId))
                .filter(c -> c.getAccObject().getContract().getId().toString().equals(contractId))
                .filter(c -> c.getAccObject().getId().toString().equals(accObjectId))
                .map(p -> modelMapper.map(p, ChargeServiceDto.class));
    }

    @Override
    public void createChargeService(String contragentId, String contractId, String accObjectId, ChargeServiceDto chargeDto) {
        var contract = contractRepository.findById(UUID.fromString(contractId));

        if (contract.isEmpty())
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!contract.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var accObject = contract
                .get()
                .getAccObjects()
                .stream()
                .filter(a -> a.getId().toString().equals(accObjectId))
                .findAny();
        if (accObject.isEmpty())
            throw new EntityNotFoundException("Accounting object not found: " + accObjectId);

        var chargeService = modelMapper.map(chargeDto, ChargeService.class);
        chargeService.setAccObject(accObject.get());
        chargeService.setId(null);
        chargeServiceRepository.save(chargeService);
        chargeDto.id(chargeService.getId().toString());
    }

    @Override
    public void updateChargeService(String contragentId, String contractId, String accObjectId, ChargeServiceDto chargeDto) {
        var charge = chargeServiceRepository.findById(UUID.fromString(chargeDto.id()));

        if (charge.isEmpty())
            throw new EntityNotFoundException("Charge not found: " + chargeDto.id());

        if (!charge.get().getAccObject().getId().toString().equals(accObjectId))
            throw new EntityNotFoundException("Accounting object not found: " + accObjectId);

        if (!charge.get().getAccObject().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!charge.get().getAccObject().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var chargeOnce = modelMapper.map(chargeDto, ChargeService.class);
        chargeOnce.setAccObject(charge.get().getAccObject());
        chargeServiceRepository.save(chargeOnce);
    }

    @Override
    public boolean deleteChargeService(String contragentId, String contractId, String accObjectId, String chargeId) {
        UUID id = UUID.fromString(chargeId);
        var charge = chargeServiceRepository.findById(id);

        if (charge.isEmpty())
            return false;

        if (!charge.get().getAccObject().getId().toString().equals(accObjectId))
            throw new EntityNotFoundException("Accounting object not found: " + accObjectId);

        if (!charge.get().getAccObject().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!charge.get().getAccObject().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        chargeServiceRepository.deleteById(id);
        return true;
    }
}
