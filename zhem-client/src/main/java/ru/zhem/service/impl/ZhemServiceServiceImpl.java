package ru.zhem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.client.interfaces.ZhemServiceRestClient;
import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.response.ZhemServiceCreationDto;
import ru.zhem.service.interfaces.ZhemServiceService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ZhemServiceServiceImpl implements ZhemServiceService {

    private final ZhemServiceRestClient restClient;

    @Override
    public List<ZhemServiceDto> findAllServices() {
        return this.restClient.findAllServices();
    }

    @Override
    public ZhemServiceDto createService(ZhemServiceCreationDto service) {
        return this.restClient.createService(service);
    }

    @Override
    public void deleteServiceById(int serviceId) {
        this.restClient.deleteServiceById(serviceId);
    }
}
