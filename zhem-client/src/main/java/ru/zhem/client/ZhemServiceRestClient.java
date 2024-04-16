package ru.zhem.client;

import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.response.ZhemServiceCreationDto;

import java.util.List;

public interface ZhemServiceRestClient {

    List<ZhemServiceDto> findAllServices();

    ZhemServiceDto createService(ZhemServiceCreationDto service);

    void deleteServiceById(int serviceId);

}
