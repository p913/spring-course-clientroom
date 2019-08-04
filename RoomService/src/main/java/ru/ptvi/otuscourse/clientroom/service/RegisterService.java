package ru.ptvi.otuscourse.clientroom.service;

import ru.ptvi.otuscourse.clientroom.domain.RegDataDto;
import ru.ptvi.otuscourse.clientroom.domain.RegResultDto;

public interface RegisterService {
    RegResultDto register(RegDataDto regDataDto);
}
