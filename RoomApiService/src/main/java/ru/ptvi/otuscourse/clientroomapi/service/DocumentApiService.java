package ru.ptvi.otuscourse.clientroomapi.service;

import ru.ptvi.otuscourse.clientroomdto.DocuLinkDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DocumentApiService {
    List<DocuLinkDto> getBetweenDates(String contragentId, String contractId, LocalDate from, LocalDate to);

    Optional<DocuLinkDto> getById(String contragentId, String contractId, String docId);

    void create(String contragentId, String contractId, DocuLinkDto docuLinkDto);

    void update(String contragentId, String contractId, DocuLinkDto docuLinkDto);

    boolean delete(String contragentId, String contractId, String docId);
}
