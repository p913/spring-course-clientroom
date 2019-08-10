package ru.ptvi.otuscourse.clientroomapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.ptvi.otuscourse.clientroomapi.domain.Service;
import ru.ptvi.otuscourse.clientroomapi.repository.ServiceRepository;
import ru.ptvi.otuscourse.clientroomdto.ServiceDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class ServiceApiServiceImpl implements ServiceApiService {
    private final ModelMapper modelMapper;

    private final ServiceRepository serviceRepository;

    public ServiceApiServiceImpl(ModelMapper modelMapper, ServiceRepository serviceRepository) {
        this.modelMapper = modelMapper;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceDto> getAll() {
        return serviceRepository
                .findAll()
                .stream()
                .map(s -> modelMapper.map(s, ServiceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ServiceDto> getById(String serviceId) {
        return serviceRepository
                .findById(UUID.fromString(serviceId))
                .map(s -> modelMapper.map(s, ServiceDto.class));
    }

    @Override
    public void create(ServiceDto serviceDto) {
        var service = modelMapper.map(serviceDto, Service.class);

        service.setId(null);
        serviceRepository.save(service);
        serviceDto.id(service.getId().toString());
    }

    @Override
    public void update(ServiceDto serviceDto) {
        var service = modelMapper.map(serviceDto, Service.class);
        serviceRepository.save(service);
    }

    @Override
    public boolean delete(String serviceId) {
        UUID id = UUID.fromString(serviceId);
        var service = serviceRepository.findById(id);
        if (service.isEmpty())
            return false;

        serviceRepository.deleteById(id);
        return true;
    }
}
