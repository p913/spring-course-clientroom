package ru.ptvi.otuscourse.clientroomapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ptvi.otuscourse.clientroomapi.domain.Contract;
import ru.ptvi.otuscourse.clientroomapi.domain.DocuLink;
import ru.ptvi.otuscourse.clientroomapi.repository.ContractRepository;
import ru.ptvi.otuscourse.clientroomapi.repository.DocuLinkRepository;
import ru.ptvi.otuscourse.clientroomdto.DocuLinkDto;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentApiServiceImpl implements DocumentApiService {
    private final DocuLinkRepository docuLinkRepository;

    private final ContractRepository contractRepository;

    private final ModelMapper modelMapper;

    public DocumentApiServiceImpl(DocuLinkRepository docuLinkRepository, ContractRepository contractRepository, ModelMapper modelMapper) {
        this.docuLinkRepository = docuLinkRepository;
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DocuLinkDto> getBetweenDates(String contragentId, String contractId, LocalDate from, LocalDate to) {
        return docuLinkRepository
                .findByContractsBetweenDates(Collections.singletonList(new Contract(UUID.fromString(contractId))),
                        from, to)
                .stream()
                .filter(d -> d.getContract().getContragent().getId().toString().equals(contragentId))
                .map(d -> modelMapper.map(d, DocuLinkDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DocuLinkDto> getById(String contragentId, String contractId, String docId) {
        return docuLinkRepository
                .findById(UUID.fromString(docId))
                .filter(d -> d.getContract().getId().toString().equals(contractId))
                .filter(d -> d.getContract().getContragent().getId().toString().equals(contragentId))
                .map(d -> modelMapper.map(d, DocuLinkDto.class));
    }

    @Override
    public void create(String contragentId, String contractId, DocuLinkDto docuLinkDto) {
        var contract = contractRepository.findById(UUID.fromString(contractId));
        if (contract.isEmpty())
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!contract.get().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var doc = modelMapper.map(docuLinkDto, DocuLink.class);
        doc.setContract(contract.get());
        doc.setId(null);
        docuLinkRepository.save(doc);
        docuLinkDto.id(doc.getId().toString());
    }

    @Override
    public void update(String contragentId, String contractId, DocuLinkDto docuLinkDto) {
        var doc = docuLinkRepository.findById(UUID.fromString(docuLinkDto.id()));

        if (doc.isEmpty())
            throw new EntityNotFoundException("Document not found: " + docuLinkDto.id());

        if (!doc.get().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!doc.get().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        var docUpdate = modelMapper.map(docuLinkDto, DocuLink.class);
        docUpdate.setContract(doc.get().getContract());
        docuLinkRepository.save(docUpdate);
    }

    @Override
    public boolean delete(String contragentId, String contractId, String docId) {
        UUID id = UUID.fromString(docId);
        var doc = docuLinkRepository.findById(id);

        if (doc.isEmpty())
            return false;

        if (!doc.get().getContract().getId().toString().equals(contractId))
            throw new EntityNotFoundException("Contract not found: " + contractId);

        if (!doc.get().getContract().getContragent().getId().toString().equals(contragentId))
            throw new EntityNotFoundException("Contragent not found: " + contragentId);

        docuLinkRepository.deleteById(id);
        return true;
    }
}
