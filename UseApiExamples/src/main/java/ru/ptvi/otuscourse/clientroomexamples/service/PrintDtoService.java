package ru.ptvi.otuscourse.clientroomexamples.service;

import feign.Contract;
import ru.ptvi.otuscourse.clientroomdto.*;

public interface PrintDtoService {
    String print(ContragentWithDetailsDto contragentWithDetailsDto);

    void print(StringBuilder stringBuilder, ContragentWithDetailsDto contragentWithDetailsDto);

    String print(ContractWithDetailsDto contractWithDetailsDto);

    void print(StringBuilder stringBuilder, ContractWithDetailsDto contractWithDetailsDto);

    String print(AccountingObjectDto accountingObjectDto);

    void print(StringBuilder stringBuilder, AccountingObjectDto accountingObjectDto);

    String print(DemandDto demandDto);

    void print(StringBuilder stringBuilder, DemandDto demandDto);

}
