package ru.ptvi.otuscourse.clientroomexamples.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import ru.ptvi.otuscourse.clientroomdto.AccountingObjectDto;
import ru.ptvi.otuscourse.clientroomdto.ContractWithDetailsDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentWithDetailsDto;
import ru.ptvi.otuscourse.clientroomdto.DemandDto;

import java.util.stream.Collectors;

@Service
public class PrintDtoServiceImpl implements PrintDtoService {
    @Override
    public String print(ContragentWithDetailsDto contragentWithDetailsDto) {
        var sb = new StringBuilder();
        print(sb, contragentWithDetailsDto);
        return sb.toString();
    }

    @Override
    public void print(StringBuilder stringBuilder, ContragentWithDetailsDto contragent) {
        stringBuilder.append("\nContragent id: ").append(contragent.id())
                    .append("\n  E-mail: ").append(contragent.email());

        if (!StringUtils.isEmpty(contragent.peopleLastName())) {
            stringBuilder.append("\n  People: ")
                        .append(contragent.peopleFirstName()).append(' ')
                        .append(contragent.peopleMiddleName()).append(' ')
                        .append(contragent.peopleLastName())
                    .append(", passport: ").append(contragent.password())
                    .append(" birthday: ").append(contragent.password());
        } else {
            stringBuilder.append("\n  Firm: ")
                    .append(contragent.firmName()).append(' ')
                    .append("\n  INN\\KPP: ").append(contragent.firmInn())
                        .append('\\').append(contragent.firmKpp())
                    .append("\n  Account: ").append(contragent.firmAccount())
                        .append("  in ").append(contragent.firmBank());
        }
        stringBuilder.append("\n  Address: ").append(contragent.address()).append('\n');

        contragent.contracts().stream().forEach(c -> print(stringBuilder, c));
    }

    @Override
    public String print(ContractWithDetailsDto contract) {
        var sb = new StringBuilder();
        print(sb, contract);
        return sb.toString();
    }

    @Override
    public void print(StringBuilder stringBuilder, ContractWithDetailsDto contract) {
        stringBuilder.append("\nContract id: ").append(contract.id())
                .append("\n № ").append(contract.number())
                    .append(" from ").append(contract.dateFrom())
                    .append(" to ").append(contract.dateTo() == null ? "∞" : contract.dateTo().toString() )
                    .append(", balance: ").append(contract.balance())
                .append("\n");

        contract.accObjects().stream().forEach(c -> print(stringBuilder, c));
    }

    @Override
    public String print(AccountingObjectDto accounting) {
        var sb = new StringBuilder();
        print(sb, accounting);
        return sb.toString();
    }

    @Override
    public void print(StringBuilder stringBuilder, AccountingObjectDto accObject) {
        stringBuilder.append("\nAccounting object id: ").append(accObject.id())
                .append("\n  ").append(accObject.name())
                    .append(" from ").append(accObject.dateFrom())
                    .append(" to ").append(accObject.dateTo() == null ? "∞" : accObject.dateTo().toString() )
                .append("\n  Service: ").append(accObject.service().name())
                .append(accObject.description() == null ? "" : "\n  " + accObject.description())
                .append("\n");
    }

    @Override
    public String print(DemandDto demand) {
        var sb = new StringBuilder();
        print(sb, demand);
        return sb.toString();
    }

    @Override
    public void print(StringBuilder stringBuilder, DemandDto demand) {
        stringBuilder.append("\nDemand id: ").append(demand.id())
                    .append(' ').append(demand.demandDateTime())
                .append("\n  ").append(demand.demandSubject())
                    .append(' ').append(demand.demandNote());
        if (demand.contract() != null)
            stringBuilder.append("\n  Contract: ").append(demand.contract().number());
        if (demand.accObject() != null)
            stringBuilder.append("\n  Accounting object: ").append(demand.accObject().name());
    }
}
