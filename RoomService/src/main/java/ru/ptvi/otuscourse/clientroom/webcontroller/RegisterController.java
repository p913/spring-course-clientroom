package ru.ptvi.otuscourse.clientroom.webcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import ru.ptvi.otuscourse.clientroom.config.RoomConfigProps;
import ru.ptvi.otuscourse.clientroom.service.RegisterServiceImpl;
import ru.ptvi.otuscourse.clientroom.domain.CompanyDto;
import ru.ptvi.otuscourse.clientroom.domain.RegDataDto;
import ru.ptvi.otuscourse.clientroom.domain.RegResultDto;

import javax.validation.Valid;

@RefreshScope
@RestController
@RequestMapping("/reg")
public class RegisterController {
    private final RegisterServiceImpl registerService;

    private final String company;

    private final String contacts;

    public RegisterController(RegisterServiceImpl registerService, RoomConfigProps props) {
        this.registerService = registerService;
        this.company = props.getCompany();
        this.contacts = props.getContacts();
    }

    @PostMapping("/register")
    public RegResultDto register(@RequestBody @Valid RegDataDto regDataDto) {
        return registerService.register(regDataDto);
    }

    @GetMapping("/company")
    public CompanyDto getContragentWithContractsWithAccObjects() {
        return new CompanyDto()
                .name(company)
                .contacts(contacts);
    }

}
