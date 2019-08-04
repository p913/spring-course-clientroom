package ru.ptvi.otuscourse.clientroomapi.service;

import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;
import ru.ptvi.otuscourse.clientroomdto.ContractDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentWithDetailsDto;

import java.util.List;
import java.util.Optional;

public interface ContragentApiService {
    List<ContragentWithDetailsDto> getContragents(boolean details);

    Optional<ContragentWithDetailsDto> getContragentById(String contragentId);

    Optional<ContragentWithDetailsDto> getContragentByEmailOrPhone(String account, boolean details);

    void createContragent(ContragentDto contragentDto);

    void updateContagent(ContragentDto contragentDto);

    void patchContagent(ContragentDto contragentDto);

    boolean deleteContragent(String contragentId);


    List<ContractDto> getContragentContracts(String contragentId);

    Optional<ContractDto> getContractById(String contragentId, String contractId);

    void createContract(String contragentId, ContractDto contractDto);

    void updateContract(String contragentId, ContractDto contractDto);

    boolean deleteContract(String contragentId, String contractId);


    List<AccountingObjectDto> getContractAccObjects(String contragentId, String contractId);

    Optional<AccountingObjectDto> getAccObjectById(String contragentId, String contractId, String accObjectId);

    void createAccObject(String contragentId, String contractId, AccountingObjectDto accountingObjectDto);

    void updateAccObject(String contragentId, String contractId, AccountingObjectDto accountingObjectDto);

    boolean deleteAccObject(String contragentId, String contractId, String accObjectId);

}
