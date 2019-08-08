package ru.ptvi.otuscourse.clientroomexamples.service;

import feign.Contract;
import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;
import ru.ptvi.otuscourse.clientroomdto.ContractDto;
import ru.ptvi.otuscourse.clientroomdto.ContractWithDetailsDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentWithDetailsDto;

public interface PrintDtoService {
    String print(ContragentWithDetailsDto contragentWithDetailsDto);

    void print(StringBuilder stringBuilder, ContragentWithDetailsDto contragentWithDetailsDto);

    String print(ContractWithDetailsDto contractWithDetailsDto);

    void print(StringBuilder stringBuilder, ContractWithDetailsDto contractWithDetailsDto);

    String print(AccountingObjectDto accountingObjectDto);

    void print(StringBuilder stringBuilder, AccountingObjectDto accountingObjectDto);
}
