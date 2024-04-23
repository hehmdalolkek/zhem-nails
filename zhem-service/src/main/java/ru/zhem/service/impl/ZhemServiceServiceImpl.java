package ru.zhem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.ZhemService;
import ru.zhem.exception.ZhemServiceNotFoundException;
import ru.zhem.exception.ZhemServiceWithDuplicateTitleException;
import ru.zhem.repository.ZhemServiceRepository;
import ru.zhem.service.interfaces.ZhemServiceService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ZhemServiceServiceImpl implements ZhemServiceService {

    private final ZhemServiceRepository zhemServiceRepository;

    @Override
    public List<ZhemService> findAllServices() {
        return this.zhemServiceRepository.findAll();
    }

    @Override
    public ZhemService createService(ZhemService service) {
        boolean titleIsExists = this.zhemServiceRepository.existsByTitle(service.getTitle().strip());
        if (titleIsExists) {
            throw new ZhemServiceWithDuplicateTitleException("Service with this title is already exists");
        }
        return this.zhemServiceRepository.save(service);
    }

    @Override
    public void deleteServiceById(int serviceId) {
        boolean isExists = this.zhemServiceRepository.existsById(serviceId);
        if (isExists) {
            this.zhemServiceRepository.deleteById(serviceId);
        } else {
            throw new ZhemServiceNotFoundException("Service not found");
        }

    }
}
