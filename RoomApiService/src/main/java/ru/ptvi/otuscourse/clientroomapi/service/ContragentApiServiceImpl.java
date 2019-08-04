package ru.ptvi.otuscourse.clientroomapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ptvi.otuscourse.clientroomapi.domain.AccountingObject;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;
import ru.ptvi.otuscourse.clientroomapi.domain.Contragent;
import ru.ptvi.otuscourse.clientroomapi.repository.AccountingObjectRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.ContractRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.ContragentRepository;
import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;
import ru.ptvi.otuscourse.clientroomdto.ContractDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentWithDetailsDto;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContragentApiServiceImpl implements ContragentApiService {
    private static final Contragent EMPTY_CONTRAGENT = new Contragent();
    private static final Contract EMPTY_CONTRACT = new Contract();

    private final ContragentRepository contragentRepository;

    private final ContractRepository contractRepository;

    private final AccountingObjectRepository accountingObjectRepository;

    private final ModelMapper modelMapper;

    public ContragentApiServiceImpl(ContragentRepository contragentRepository, ContractRepository contractRepository, AccountingObjectRepository accountingObjectRepository, ModelMapper modelMapper) {
        this.contragentRepository = contragentRepository;
        this.contractRepository = contractRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ContragentWithDetailsDto> getContragents(boolean details) {
        return contragentRepository
                .findAll()
                .stream()
                .map(c -> {
                    if (!details)
                        c.setContracts(Collections.EMPTY_LIST);
                    return modelMapper.map(c, ContragentWithDetailsDto.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContragentWithDetailsDto> getContragentById(String contragentId) {
        return contragentRepository
                .findById(UUID.fromString(contragentId))
                .map(c -> modelMapper.map(c, ContragentWithDetailsDto.class));
    }

    @Override
    public Optional<ContragentWithDetailsDto> getContragentByEmailOrPhone(String account, boolean details) {
        return contragentRepository
                .findByEmailOrPhone(account)
                .map(c -> {
                    if (!details)
                        c.setContracts(Collections.EMPTY_LIST);
                    return modelMapper.map(c, ContragentWithDetailsDto.class);
                });
    }

    @Override
    public void createContragent(ContragentDto contragentDto) {
        contragentDto.id(null);

        var created = contragentRepository.save(modelMapper.map(contragentDto, Contragent.class));
        contragentDto.id(created.getId().toString());
    }

    @Override
    public void updateContagent(ContragentDto contragentDto) {
        if (!contragentRepository.existsById(UUID.fromString(contragentDto.id())))
            throw new EntityNotFoundException("Contragent not found: " + contragentDto.id());
        contragentRepository.save(modelMapper.map(contragentDto, Contragent.class));
    }

    @Override
    public void patchContagent(ContragentDto contragentDto) {
        var contragent = contragentRepository.findById(UUID.fromString(contragentDto.id()));

        if (contragent.isEmpty())
            throw new EntityNotFoundException("Contragent not found: " + contragentDto.id());

        //TODO все поля патчить и написать тест

        if (contragentDto.password() != null)
            contragent.get().setPassword(contragentDto.password());

        contragentRepository.save(contragent.get());
    }

    @Override
    public boolean deleteContragent(String contragentId) {
        UUID id = UUID.fromString(contragentId);

        if (!contragentRepository.existsById(id))
            return false;

        contragentRepository.deleteById(id);
        return true;
    }

    @Override
    public List<ContractDto> getContragentContracts(String contragentId) {
        return contragentRepository
                .findById(UUID.fromString(contragentId))
                .orElse(EMPTY_CONTRAGENT)
                .getContracts()
                .stream()
                .map(c -> modelMapper.map(c, ContractDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContractDto> getContractById(String contragentId, String contractId) {
        var r= contractRepository
                .findById(UUID.fromString(contractId))
                .filter(c -> c.getContragent().getId().toString().equals(contragentId))
                .map(c -> modelMapper.map(c, ContractDto.class));
        return r;
    }

    @Override
    public void createContract(String contragentId, ContractDto contractDto) {
        var contragent = contragentRepository.findById(UUID.fromString(contragentId));
        if (contragent.isEmpty())
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var contract = modelMapper.map(contractDto, Contract.class);
        contract.setContragent(contragent.get());
        contract.setId(null);
        contractRepository.save(contract);
        contractDto.id(contract.getId().toString());
    }

    @Override
    public void updateContract(String contragentId, ContractDto contractDto) {
        var contragent = contragentRepository.findById(UUID.fromString(contragentId));
        if (contragent.isEmpty())
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        if (!contragent
                .get()
                .getContracts()
                .stream()
                .anyMatch(c -> c.getId().toString().equals(contractDto.id())))
            throw new EntityNotFoundException("Contract not found: " + contractDto.id());

        var contract = modelMapper.map(contractDto, Contract.class);
        contract.setContragent(contragent.get());
        contractRepository.save(contract);
    }

    @Override
    public boolean deleteContract(String contragentId, String contractId) {
        var contract = contractRepository.findById(UUID.fromString(contractId));

        if (contract.isEmpty())
            return false;

        if (!contract.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        contractRepository.deleteById(UUID.fromString(contractId));
        return true;
    }


    @Override
    public List<AccountingObjectDto> getContractAccObjects(String contragentId, String contractId) {
        return contractRepository
                .findById(UUID.fromString(contractId))
                .orElse(EMPTY_CONTRACT)
                .getAccObjects()
                .stream()
                .filter(a -> a.getContract().getContragent().getId().toString().equals(contragentId))
                .map(c -> modelMapper.map(c, AccountingObjectDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountingObjectDto> getAccObjectById(String contragentId, String contractId, String accObjectId) {
        return accountingObjectRepository
                .findById(UUID.fromString(accObjectId))
                .filter(c -> c.getContract().getId().toString().equals(contractId))
                .filter(c -> c.getContract().getContragent().getId().toString().equals(contragentId))
                .map(c -> modelMapper.map(c, AccountingObjectDto.class));
    }

    @Override
    public void createAccObject(String contragentId, String contractId, AccountingObjectDto accountingObjectDto) {
        var contragent = contragentRepository.findById(UUID.fromString(contragentId));
        if (contragent.isEmpty())
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var contract = contragent
                .get()
                .getContracts()
                .stream()
                .filter(c -> c.getId().toString().equals(contractId))
                .findAny();
        if (contract.isEmpty())
            throw new EntityNotFoundException("Contract not found: " + contractId);

        var accObject = modelMapper.map(accountingObjectDto, AccountingObject.class);
        accObject.setContract(contract.get());
        accObject.setId(null);
        accountingObjectRepository.save(accObject);
        accountingObjectDto.id(accObject.getId().toString());
    }

    @Override
    public void updateAccObject(String contragentId, String contractId, AccountingObjectDto accountingObjectDto) {
        var contragent = contragentRepository.findById(UUID.fromString(contragentId));
        if (contragent.isEmpty())
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var contract = contragent
                .get()
                .getContracts()
                .stream()
                .filter(c -> c.getId().toString().equals(contractId))
                .findAny();
        if (contract.isEmpty())
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!contract.get().getAccObjects().stream().anyMatch(a -> a.getId().toString().equals(accountingObjectDto.id())))
            throw new EntityNotFoundException("Accounting object not found: " + accountingObjectDto.id());

        var accObject = modelMapper.map(accountingObjectDto, AccountingObject.class);
        accObject.setContract(contract.get());
        accountingObjectRepository.save(accObject);
    }

    @Override
    public boolean deleteAccObject(String contragentId, String contractId, String accObjectId) {
        UUID id = UUID.fromString(accObjectId);
        var accObject = accountingObjectRepository.findById(id);

        if (accObject.isEmpty())
            return false;

        if (!accObject.get().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!accObject.get().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        accountingObjectRepository.deleteById(id);
        return true;
    }
}
