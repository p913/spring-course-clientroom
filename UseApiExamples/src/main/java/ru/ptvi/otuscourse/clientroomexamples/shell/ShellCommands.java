package ru.ptvi.otuscourse.clientroomexamples.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.ptvi.otuscourse.clientroomexamples.service.PrintDtoService;
import ru.ptvi.otuscourse.clientroomexamples.service.UseApiService;

import java.util.stream.Collectors;

@ShellComponent
public class ShellCommands {
    private final UseApiService useApiService;

    private final PrintDtoService printDtoService;

    public ShellCommands(UseApiService useApiService, PrintDtoService printDtoService) {
        this.useApiService = useApiService;
        this.printDtoService = printDtoService;
    }

    @ShellMethod(value = "Show contragent by id with contracts and accounting objects", key = "contragent-show")
    public String getContragentById(@ShellOption("Contragent Id") String id) {
        var contragent = useApiService.getContragentById(id);
        if (contragent.isEmpty())
            return "Not found";
        else
            return printDtoService.print(contragent.get());
    }

    @ShellMethod(value = "Search contragents by email or phone", key = "contragent-search")
    public String searchContragentByEmailOrPhone(@ShellOption("Email or phone") String account) {
        return useApiService.getContragentsByEmailOrPhone(account)
                .stream()
                .map(printDtoService::print)
                .collect(Collectors.joining());
    }

    @ShellMethod(value = "Create contragent as people with default accounting object and service", key = "contragent-create")
    public String createContragent(@ShellOption("E-mail") String email,
                                   @ShellOption("Phone") String phone,
                                   @ShellOption("First Name") String firstName,
                                   @ShellOption("Last Name") String lastName,
                                   @ShellOption("Contract number") String contract,
                                   @ShellOption(value = "Post address", defaultValue = "") String address) {
        return "Created with id: " +
                useApiService.createContragentAsPeople(email, phone, firstName, null, lastName, contract, address);
    }

    @ShellMethod(value = "Create contragent as people with default accounting object and service", key = "contragent-charge")
    public String chargeContragent(@ShellOption("Contragent Id") String id,
                                   @ShellOption("Year of charge period ") int year,
                                   @ShellOption("Month of charge period ") int month) {
        useApiService.chargeAndNotify(id, year, month);
        return "Done.";
    }

    @ShellMethod(value = "Show all opened demands", key = "demand-show-opened")
    public String showOpenDemand(@ShellOption("Contragent Id") String id) {
        return useApiService
                .getContragentOpenedDemands(id)
                .stream()
                .map(printDtoService::print)
                .collect(Collectors.joining());
    }

    @ShellMethod(value = "Make decision for demand", key = "demand-close")
    public String showOpenDemand(@ShellOption("Contragent Id") String contragentId,
                                 @ShellOption("Demand Id") String demandId,
                                 @ShellOption("Success") boolean success) {
        useApiService.closeDemand(contragentId, demandId, success, "И не благодарите");
        return "Done.";
    }

}
