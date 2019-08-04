package ru.ptvi.otuscourse.clientroom.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ptvi.otuscourse.clientroom.domain.RegDataDto;
import ru.ptvi.otuscourse.clientroom.domain.RegResultDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentDto;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final RoomApiService roomApiService;

    private final RoomApiAuthSevice roomApiAuthSevice;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public RegisterServiceImpl(RoomApiService roomApiService, RoomApiAuthSevice roomApiAuthSevice) {
        this.roomApiService = roomApiService;
        this.roomApiAuthSevice = roomApiAuthSevice;
    }

    @Override
    public RegResultDto register(RegDataDto regDataDto) {
        var contragent = roomApiService.getContragentByEmailOrPhone(roomApiAuthSevice.getAuthHeader(), regDataDto.username(), true);

        if (contragent.size() != 1)
            return new RegResultDto().success(false).error("Необходимо указать e-mail или телефон точно так же, как в договоре");

        if (!contragent.get(0)
                .contracts()
                .stream()
                .anyMatch(c -> c.dateFrom().toString().equals(regDataDto.date()) &&
                        c.number().equals(regDataDto.contract())))
            return new RegResultDto().success(false).error("Проверьте введенные номер и дату договора");

        roomApiService.patchContragent(roomApiAuthSevice.getAuthHeader(), contragent.get(0).id(),
                new ContragentDto().password(passwordEncoder.encode(regDataDto.password())));

        return new RegResultDto().success(true);
    }
}
